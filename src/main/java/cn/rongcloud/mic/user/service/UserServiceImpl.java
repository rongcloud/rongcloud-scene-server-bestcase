package cn.rongcloud.mic.user.service;

import cn.rongcloud.common.im.IMHelper;
import cn.rongcloud.common.im.pojos.IMApiResultInfo;
import cn.rongcloud.common.im.pojos.IMTokenInfo;
import cn.rongcloud.common.sms.SMSHelper;
import cn.rongcloud.common.sms.pojos.SMSCodeVerifyResult;
import cn.rongcloud.common.sms.pojos.SMSSendCodeResult;
import cn.rongcloud.common.sms.pojos.VerificationCodeInfo;
import cn.rongcloud.common.utils.DateTimeUtils;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.HttpHelper;
import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.common.utils.WXUtils;
import cn.rongcloud.mic.authorization.pojos.ResLoginWX;
import cn.rongcloud.mic.authorization.pojos.WXDecodeInfo;
import cn.rongcloud.mic.common.constant.CustomerConstant;
import cn.rongcloud.mic.common.jwt.JwtTokenHelper;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.utils.JwtUtils;
import cn.rongcloud.mic.common.rest.RestException;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.community.entity.TCommunityUser;
import cn.rongcloud.mic.community.message.CommunityMessageUtil;
import cn.rongcloud.mic.community.service.TCommunityUserService;
import cn.rongcloud.mic.shumei.key.ShumeiKey;
import cn.rongcloud.mic.shumei.service.ShumeiService;
import cn.rongcloud.mic.user.constant.UserKeysCons;
import cn.rongcloud.mic.user.dao.UserDao;
import cn.rongcloud.mic.user.enums.UserStatusEnum;
import cn.rongcloud.mic.user.enums.UserType;
import cn.rongcloud.mic.user.message.LoginDeviceMessage;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.model.TUserFollow;
import cn.rongcloud.mic.user.pojos.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserFollowService userFollowService;

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    IMHelper imHelper;

    @Autowired
    SMSHelper smsHelper;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Value("${rongrtc.login.sms_verify}")
    private boolean loginSMSVerify;

    @Value("${sms.templateId}")
    private String smsTemplateId;

    @Value("${sms.foreignSmstmpid}")
    private String foreignSmstmpid;

    @Value("${rongrtc.adminURL}")
    private String adminUrl;

    @Value("${queryMobileLimit}")
    private int queryMobileLimit;

    @Value("${domainUrl}")
    private String domainUrl;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ShumeiService shumeiService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private CommunityMessageUtil communityMessageUtil;

    @Autowired
    private TCommunityUserService tCommunityUserService;

    private RedisUtils redisUtils;

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    public RestResult login(ReqLogin data, HttpServletRequest httpReq) {
        return this.login(data, true, httpReq);
    }

    private void sendDeviceMessage(String userId,String platform){
        // 用户登录发送登录设备信息
        LoginDeviceMessage deviceMessage = new LoginDeviceMessage();
        deviceMessage.setUserId(userId);
        deviceMessage.setPlatform(platform);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, Lists.newArrayList(userId),deviceMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RestResult<ResLoginWX> loginWXV2(ReqLoginWX data, HttpServletRequest httpReq) {

        String sessionKey = (String) redisUtils.get(CustomerConstant.WX_SESSION_KEY_CACHE_PREFIX + data.getOpenId());
        if (StringUtils.isEmpty(sessionKey)) {
            return RestResult.generic(RestResultCode.ERR_WX_SESSION_KEY_INVALID);
        }

        JSONObject decodeInfo = WXUtils.getDecodeInfo(data.getEncryptedData(), sessionKey,
            data.getIv());
        if (Objects.isNull(decodeInfo)) {
            return RestResult.generic(RestResultCode.ERR_ENCRYPTED_DATA_DECODE_FAILED);
        }
        WXDecodeInfo wxDecodeInfo = decodeInfo.toJavaObject(WXDecodeInfo.class);
        RestResult<ResLogin> login = login(ReqLogin.builder()
            .mobile(wxDecodeInfo.getPurePhoneNumber())
            // 小程序好像获取不到设备id
            .deviceId(data.getOpenId())
            .build(), false, httpReq);
        ResLogin resLogin = login.getResult();
        return RestResult.success(ResLoginWX.buildResLoginWX(resLogin, wxDecodeInfo));
    }

    @Override
    public RestResult<ResLogin> loginWX(ReqLogin data,HttpServletRequest httpReq) {
        return this.login(data, false,httpReq);
    }


    /**
     * 登录
     *
     * @param data        登录用户信息
     * @param isSMSVerify 是否手机验证码
     * @return
     */
    private RestResult<ResLogin> login(ReqLogin data, Boolean isSMSVerify,HttpServletRequest httpReq) {
        //检查手机号格式
        if (!isMobile(data.getMobile())) {
            return RestResult.generic(RestResultCode.ERR_USER_INVALID_PHONE_NUMBER);
        }

        if (isSMSVerify) {
            //校验短信验证码是否开启
            if (loginSMSVerify) {
                if (data.getVerifyCode() == null) {
                    throw new RestException(RestResult.generic(RestResultCode.ERR_USER_VERIFY_CODE_EMPTY));
                }
                RestResultCode result = verifyCode(data.getMobile(), data.getVerifyCode());
                if (result.getCode() != RestResultCode.ERR_SUCCESS.getCode()) {
                    return RestResult.generic(result);
                }
            }
        }
        TUser user = noExitSaveUser(data);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        if(user.getStatus()!=null&&user.getStatus().intValue()== UserStatusEnum.FREEZE.getValue()&&user.getFreezeTime().after(new Date())){
            return RestResult.generic(80000,String.format(ShumeiKey.FREEZE_MESSAGE,sdf.format(user.getFreezeTime())));
        }

        //构建 jwtUser 对象
        JwtUser jwtUser = build(user);
        log.info("==login:jwtUser:{}", JSON.toJSONString(jwtUser));
        //封装登录返回结果
        ResLogin loginResult = build(jwtUser);
        log.info("==login:loginResult:{}", JSON.toJSONString(loginResult));

        sendDeviceMessage(user.getUid(),data.getPlatform());

        loginResult.setSex(user.getSex()==null?1:user.getSex());
        userLoginService.saveByUserId(user.getUid(),httpReq);
        return RestResult.success(loginResult);
    }


    @Override
    public RestResult<ResLogin> visitorLogin(ReqVisitorLogin data) {
        TUser user = userDao.findTUserByDeviceIdEqualsAndTypeEquals(data.getDeviceId(), UserType.VISITOR.getValue());
        Date curDate = DateTimeUtils.currentUTC();
        if (user == null) {
            user = new TUser();
            user.setUid(IdentifierUtils.uuid36());
            user.setName(data.getUserName());
            user.setDeviceId(data.getDeviceId());
            user.setPortrait(data.getPortrait());
            user.setType(UserType.VISITOR.getValue());
            user.setCreateDt(curDate);
            user.setUpdateDt(curDate);
            //保存游客信息至数据库
            userDao.save(user);
        }

        //构建 jwtUser 对象
        JwtUser jwtUser = build(user);

        //封装登录返回结果
        ResLogin loginResult = build(jwtUser);

        return RestResult.success(loginResult);
    }

    private JwtUser build(TUser user) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUserId(user.getUid());
        jwtUser.setUserName(user.getName());
        jwtUser.setPortrait(user.getPortrait());
        jwtUser.setType(user.getType());
        return jwtUser;
    }

    private ResLogin build(JwtUser jwtUser) {
        ResLogin loginResult = new ResLogin();
        //loginResult.setAuthorization(jwtTokenHelper.createJwtToken(jwtUser).getToken());
        loginResult.setAuthorization(JwtUtils.generateToken(CustomerConstant.SERVICENAME,GsonUtil.toJson(jwtUser)));
        loginResult.setImToken(getIMToken(jwtUser));
        loginResult.setUserName(jwtUser.getUserName());
        loginResult.setPortrait(jwtUser.getPortrait());
        loginResult.setType(jwtUser.getType());
        loginResult.setUserId(jwtUser.getUserId());
        loginResult.setCurrentTime(System.currentTimeMillis());
        return loginResult;
    }

    @Override
    public RestResult<ResRefreshToken> refreshIMToken(JwtUser jwtUser) {
        ResRefreshToken refreshToken = new ResRefreshToken();
        refreshToken.setImToken(refreshToken(jwtUser));
        return RestResult.success(refreshToken);
    }

    @Override
    public RestResult sendCode(String mobile) {
        if (!loginSMSVerify) {
            //TOTO 需要优化
            return RestResult.success();
        }
        return RestResult.generic(sendVerifyCode(mobile));
    }

    @Override
    public TUser getUserInfo(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return null;
        }
        TUser user = (TUser) redisTemplate.opsForValue().get(getRedisUserInfoKey(uid));
        if (user == null) {
            user = userDao.findTUserByUidEquals(uid);
            log.info("get user from db, userId:{}", uid);
        }
        log.info("get user from redis, userId:{}", uid);
        if (user != null) {
            redisTemplate.opsForValue().set(getRedisUserInfoKey(uid), user);
        }
        return user;
    }

    @Override
    public RestResult<List<ResFollowInfo>> batchGetUsersInfo(ReqUserIds data, JwtUser jwtUser) {

        List<ResFollowInfo> outs = new ArrayList<>();
        //获取当前人员中我关注的人员
        Page<TUserFollow> followList = userFollowService.findByUidList(jwtUser.getUserId(), 1, 200, 2, data.getUserIds());
        List<TUserFollow> fans = followList.getContent();
        List<String> collect = fans.stream().map(TUserFollow::getFollowUid).collect(Collectors.toList());
        for (String userId : data.getUserIds()) {
            ResFollowInfo out = new ResFollowInfo();
            TUser user = getUserInfo(userId);
            if (user != null) {
                out.setUserId(userId);
                out.setUserName(user.getName());
                out.setPortrait(user.getPortrait());
                out.setStatus(0);//获取人员列表返回单签人员是否已关注
                if (collect.contains(userId)) {
                    out.setStatus(1);
                }
                outs.add(out);
            }
        }
        return RestResult.success(outs);
    }

    @Override
    public RestResult update(ReqUpdate data, JwtUser jwtUser) {
        String uid = jwtUser.getUserId();
        if (StringUtils.isEmpty(uid)) {
            return null;
        }
        TUser user = getUserInfo(uid);
        if (!StringUtils.isBlank(data.getPortrait())) {
            user.setPortrait(data.getPortrait());
            String path = "https://" + domainUrl+"/file/show?path="+data.getPortrait();
            RestResult restResult = shumeiService.imageAudit(path);
            if(restResult.getCode()!=RestResultCode.ERR_SUCCESS.getCode()){
                return restResult;
            }
            List<TCommunityUser> communityUsers = tCommunityUserService.queryByUserId(user.getUid());
            for(TCommunityUser communityUser:communityUsers){
                communityMessageUtil.sendUserUpdateMessage(user.getUid(),Lists.newArrayList(communityUser.getCommunityUid()),user.getPortrait(),communityUser.getNickName(),1);
            }



        }
        if (!StringUtils.isBlank(data.getUserName())) {
            user.setName(data.getUserName());
            RestResult restResult = shumeiService.textAudit(data.getUserName());
            if(restResult.getCode()!=RestResultCode.ERR_SUCCESS.getCode()){
                return restResult;
            }
        }
        if(data.getSex()!=null){
            user.setSex(data.getSex());
        }
        userDao.updateUser(uid, user.getName(), user.getPortrait(),user.getSex());
        if (user != null) {
            redisTemplate.opsForValue().set(getRedisUserInfoKey(uid), user);
        }
        return RestResult.success(user);
    }

    @Override
    public RestResult<ResUserInfo> getUserByMobile(String mobile, JwtUser jwtUser) {
        if (StringUtils.isBlank(mobile)) {
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        String userId = jwtUser.getUserId();
        if (StringUtils.isBlank(userId)) {
            return RestResult.generic(RestResultCode.ERR_OTHER, "userId not exist");
        }
        Integer num = this.getSearchNum(userId);
        //判断是否>20次
        if (num.compareTo(queryMobileLimit) > 0) {
            return RestResult.generic(RestResultCode.ERR_USER_SEARCH_OUT_NUMBER);
        }
        log.info("{} search user num :{} ", userId, num);
        TUser user = userDao.findTUserByMobileEquals(mobile);
        if (Objects.isNull(user)) {
            return RestResult.generic(RestResultCode.ERR_OTHER, "user not exist");
        }
        //增加查询次数
        this.addSearchNum(userId, num + 1);
        return RestResult.success(this.buildUserInfo(user));
    }

    @Override
    public RestResult changeStatus(String roomId, JwtUser jwtUser) {
        TUser user = this.getUserInfo(jwtUser.getUserId());
        if (user == null) {
            return RestResult.generic(RestResultCode.ERR_USER_IM_TOKEN_ERROR);
        }
        //TODO：此处没有检查room是否有效。
        user.setBelongRoomId(roomId);
        userDao.updateBelongRoomId(jwtUser.getUserId(), roomId);
        //更新缓存
        redisTemplate.opsForValue().set(getRedisUserInfoKey(jwtUser.getUserId()), user);
        return RestResult.success();
    }

    @Override
    public RestResult checkStatus(JwtUser jwtUser) {
        TUser user = this.getUserInfo(jwtUser.getUserId());
        if (user == null) {
            return RestResult.generic(RestResultCode.ERR_USER_IM_TOKEN_ERROR);
        }
//        if (StringUtils.isNotBlank(user.getBelongRoomId())) {
//            RestResult restResult = roomDao.getRoomDetail(user.getBelongRoomId());
//            if (restResult.getCode() == RestResultCode.ERR_SUCCESS.getCode()) {
//                return RestResult.success(restResult.getResult());
//            }
//        }
        return RestResult.success(user);
    }


    private ResUserInfo buildUserInfo(TUser user) {
        if (user == null) {
            return null;
        }
        ResUserInfo info = new ResUserInfo();
        info.setUid(user.getUid());
        info.setName(user.getName());
        info.setPortrait(user.getPortrait());
        info.setMobile(user.getMobile());
        info.setType(user.getType());
        info.setDeviceId(user.getDeviceId());
        info.setCreateDt(user.getCreateDt());
        info.setUpdateDt(user.getUpdateDt());
        info.setBelongRoomId(user.getBelongRoomId());
        return info;
    }

    private Integer getSearchNum(String userId) {
        Object val = redisTemplate.opsForValue().get(this.getSearchNumKey(userId));
        if (val == null)
            return 0;
        return (Integer) val;
    }

    private void addSearchNum(String userId, Integer num) {
        long second = this.getRemainSecondsOneDay(new Date());

        redisTemplate.opsForValue().set(this.getSearchNumKey(userId), num, second, TimeUnit.SECONDS);
    }

    /**
     * 当前时间距离今天结束还剩下多少秒（单位秒）
     *
     * @param currentDate
     * @return
     */
    public static Integer getRemainSecondsOneDay(Date currentDate) {
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    public String getIMToken(JwtUser jwtUser) {

        //先从缓存查，token是否存在
        String token = (String) redisTemplate.opsForHash().get(getRedisUserIMTokenKey(), jwtUser.getUserId());
        if (token != null) {
            return token;
        }
        return refreshToken(jwtUser);
    }

    private String refreshToken(JwtUser jwtUser) {
        //从 IM 重新获取 token
        try {
            IMTokenInfo tokenInfo = imHelper.getToken(jwtUser.getUserId(), jwtUser.getUserName(), jwtUser.getPortrait());
            if (!tokenInfo.isSuccess()) {
                throw new RestException(RestResult.generic(RestResultCode.ERR_USER_IM_TOKEN_ERROR, tokenInfo.getErrorMessage()));
            }
            //将 IM token 放入 Redis 缓存
            redisTemplate.opsForHash().put(getRedisUserIMTokenKey(), jwtUser.getUserId(), tokenInfo.getToken());

            return tokenInfo.getToken();
        } catch (Exception e) {
            log.info("request token error, e:{}", e.getMessage());
        }
        return null;
    }

    private String getRedisUserInfoKey(String userId) {
        return CustomerConstant.SERVICENAME + "|user_info|" + userId;
    }

    private String getSearchNumKey(String userId) {
        return CustomerConstant.SERVICENAME + "|user_search|" + userId;
    }

    private String getRedisUserIMTokenKey() {
        return CustomerConstant.SERVICENAME + "|user_im_token";
    }


    private RestResultCode sendVerifyCode(String mobile) {
        //检查手机号格式
        if (!isMobile(mobile)) {
            return RestResultCode.ERR_USER_INVALID_PHONE_NUMBER;
        }

        try {
            VerificationCodeInfo codeInfo = (VerificationCodeInfo) redisTemplate.opsForValue().get(getRedisSMSVerifyCodeKey(mobile));
            if (codeInfo != null) {
                Date now = new Date();
                // Todo Over frequency time should read from config
                if (now.getTime() - codeInfo.getCreateDt().getTime() < 60 * 1000) {
                    return RestResultCode.ERR_USER_SEND_CODE_OVER_FREQUENCY;
                }
            } else {
                codeInfo = new VerificationCodeInfo();
            }
            codeInfo.setCreateDt(new Date());

            String sessionId = null;
            String templateId = smsTemplateId;
            SMSSendCodeResult sendCodeResult = smsHelper.sendCode(mobile, templateId, "+86", null, null);
            if (sendCodeResult == null) {
                log.info("---sendCodeResult is null");
                return RestResultCode.ERR_USER_FAILURE_EXTERNAL;
            }
            if (sendCodeResult.getCode() == 200 && sendCodeResult.getSessionId() != null) {
                log.info("---sendCodeResult: " + GsonUtil.toJson(sendCodeResult));
                sessionId = sendCodeResult.getSessionId();
            } else if(sendCodeResult.getCode().intValue() == 1008){
                return RestResultCode.ERR_USER_SEND_CODE_OVER_FREQUENCY;
            }else {
                log.error("sms code error, code = {}, msg = {} ", sendCodeResult.getCode(), sendCodeResult.getErrorMessage());
                return RestResultCode.ERR_USER_FAILURE_EXTERNAL;
            }

            codeInfo.setSessionId(sessionId);
            redisTemplate.opsForValue().set(getRedisSMSVerifyCodeKey(mobile), codeInfo, 15, TimeUnit.MINUTES);
            return RestResultCode.ERR_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultCode.ERR_USER_FAILURE_EXTERNAL;
        }
    }

    private RestResultCode verifyCode(String mobile, String code) {

        if (!isMobile(mobile)) {
            return RestResultCode.ERR_USER_INVALID_PHONE_NUMBER;
        }

        VerificationCodeInfo codeInfo = (VerificationCodeInfo) redisTemplate.opsForValue().get(getRedisSMSVerifyCodeKey(mobile));
        if (codeInfo == null) {
            return RestResultCode.ERR_USER_NOT_SEND_CODE;
        }

        SMSCodeVerifyResult codeResult = smsHelper.verifyCode(codeInfo.getSessionId(), code);
        if (!(codeResult.getCode() == 200 && codeResult.isSuccess())) {
            log.info("verify code error, sessionId = {}, code = {} , error = {}, msg = {}",
                    codeInfo.getSessionId(), code, codeResult.getCode(), codeResult.getErrorMessage());
            return RestResultCode.ERR_USER_VERIFY_CODE_INVALID;
        }

        redisTemplate.delete(getRedisSMSVerifyCodeKey(mobile));

        return RestResultCode.ERR_SUCCESS;
    }

    private String getRedisSMSVerifyCodeKey(String mobile) {
        return CustomerConstant.SERVICENAME + "_sms_verifycode_" + mobile;
    }

    /**
     * 是否为手机号
     */
    public static boolean isMobile(String str) {
        final String regex = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        //对比
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(str.trim());
        return mat.matches();
    }

    @Override
    public RestResult sendBroadcastMessage(String msg) {
        try {
            //(String fromUserId, String objectName, String content)
            Map messagePut = new HashMap();
            messagePut.put("content", msg);
            IMApiResultInfo resultInfo = imHelper.publishBroadcastMessage("_SYSTEM_", "RC:TxtMsg", GsonUtil.toJson(messagePut));
            if (resultInfo.getCode().equals(200)) {
                return RestResult.success();
            } else {
                return RestResult.generic(RestResultCode.ERR_OTHER);
            }

        } catch (Exception e) {
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
    }

    /**
     * @Description:微信小程序登录
     * @Param: [data]
     * @return: cn.rongcloud.mic.common.rest.RestResult
     * @Author: gaoxin
     * @Date: 2021/9/3
     */
    @Override
    public RestResult wechatAppletLogin(ReqLogin data) {
        String mobile = data.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }

        log.info("{} wechatAppletLogin{},GsonUtil.toJson(data)");
        TUser user = userDao.findTUserByMobileEquals(mobile);
        if (user != null) {
            //构建 jwtUser 对象
            JwtUser jwtUser = build(user);

            //封装登录返回结果
            ResLogin loginResult = build(jwtUser);

            return RestResult.success(loginResult);
        } else {
            Date curDate = DateTimeUtils.currentUTC();
            String name = data.getUserName();
            if (name == null || "".equals(name)) {
                name = "用户" + StringUtils.substring(data.getMobile(), 7);
            }
            user = new TUser();
            user.setUid(IdentifierUtils.uuid36());
            user.setName(name);
            user.setMobile(data.getMobile());
            user.setDeviceId(data.getDeviceId());
            user.setPortrait(data.getPortrait());
            user.setType(UserType.USER.getValue());
            user.setCreateDt(curDate);
            user.setUpdateDt(curDate);
            //用户信息保存至数据库
            userDao.save(user);

            //构建 jwtUser 对象
            JwtUser jwtUser = build(user);

            //封装登录返回结果
            ResLogin loginResult = build(jwtUser);

            return RestResult.success(loginResult);
        }

    }

    @Override
    @Transactional
    public RestResult resign(JwtUser jwtUser) {
        String uid = jwtUser.getUserId();
        if (StringUtils.isEmpty(uid)) {
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        TUser user = getUserInfo(uid);
        if(user==null){
            return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
        }
        userDao.delete(user);
        userFollowService.deleteByUserId(user.getUid());
        return RestResult.success();
    }

    @Override
    public RestResult sendInternationalCode(ReqSendCode data) {
        if(data==null||StringUtils.isBlank(data.getMobile())||StringUtils.isBlank(data.getRegion())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        String code = (String) redisTemplate.opsForValue().get(String.format(UserKeysCons.REDIS_INTERNATIONAL_SMS_CODE, data.getMobile()));
        if(StringUtils.isNotBlank(code)){
            return RestResult.generic(RestResultCode.ERR_USER_SEND_CODE_OVER_FREQUENCY);
        }
        String verifyCode = String.valueOf((int)((Math.random()*900000+100000)));
        String templateId = foreignSmstmpid;
        SMSSendCodeResult smsSendCodeResult = null;
        try {
            smsSendCodeResult= smsHelper.sendInternationalCode(data.getMobile(),templateId,data.getRegion(),verifyCode);
        } catch (Exception e) {
            log.error("sendInternationalCode error message:{}",e.getMessage(),e);
        }
        if(smsSendCodeResult==null||smsSendCodeResult.getCode()!=200){
            log.error("sendInternationalCode phone:{},message:{}",data.getMobile(),smsSendCodeResult.getErrorMessage());
            return RestResult.generic(smsSendCodeResult.getCode(),"您输入的手机号码格式无效");
        }

        redisTemplate.opsForValue().set(String.format(UserKeysCons.REDIS_INTERNATIONAL_SMS_CODE, data.getMobile()),verifyCode,2, TimeUnit.MINUTES);
        return RestResult.success();
    }

    @Override
    public RestResult<ResLogin> internationalLogin(ReqLogin data,boolean isSMSVerify,HttpServletRequest httpReq) {
        if (isSMSVerify) {
            //校验短信验证码是否开启
            if (loginSMSVerify) {
                if (data.getVerifyCode() == null) {
                    return RestResult.generic(RestResultCode.ERR_USER_VERIFY_CODE_EMPTY);
                }
                String verifyCode = (String) redisTemplate.opsForValue().get(String.format(UserKeysCons.REDIS_INTERNATIONAL_SMS_CODE, data.getMobile()));

                if(StringUtils.isBlank(verifyCode)){
                    return RestResult.generic(RestResultCode.ERR_USER_SMS_INVALID);
                }
                if(!data.getVerifyCode().equals(verifyCode)){
                    return RestResult.generic(RestResultCode.ERR_USER_VERIFY_CODE_INVALID);
                }
            }
        }

        TUser user = noExitSaveUser(data);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        if(user.getStatus()!=null&&user.getStatus().intValue()== UserStatusEnum.FREEZE.getValue()&&user.getFreezeTime().after(new Date())){
            return RestResult.generic(80000,String.format(ShumeiKey.FREEZE_MESSAGE,sdf.format(user.getFreezeTime())));
        }
        //构建 jwtUser 对象
        JwtUser jwtUser = build(user);
        //封装登录返回结果
        ResLogin loginResult = build(jwtUser);
        log.info("==login:loginResult:{}", JSON.toJSONString(loginResult));
        //发送Demo用户体验接口
        //this.requestAdmin(data.getMobile());

        sendDeviceMessage(user.getUid(),data.getPlatform());
        loginResult.setSex(user.getSex()==null?1:user.getSex());
        userLoginService.saveByUserId(user.getUid(),httpReq);
        return RestResult.success(loginResult);
    }

    @Override
    public RestResult<ResLogin> loginDevice(String platform, String userId,HttpServletRequest httpReq) {
        sendDeviceMessage(userId, platform);
        userLoginService.saveByUserId(userId,httpReq);
        return RestResult.success();
    }

    @Override
    public void freezeUser(String userId, Integer status, Date freezeTime) {
        userDao.freezeUser(userId, status, freezeTime);
    }

    private TUser noExitSaveUser(ReqLogin data){
        //默认头像为空
        if (data.getPortrait() == null) {
            data.setPortrait("");
        }

        Date curDate = DateTimeUtils.currentUTC();
        //根据手机号检查用户是否存在
        TUser user = userDao.findTUserByMobileEquals(data.getMobile());
        log.info("==login:user:{},data:{}", JSON.toJSONString(user), JSON.toJSONString(data));
        if (user == null) {
            String name = data.getUserName();
            if (name == null || "".equals(name)) {
                name = "用户" + StringUtils.substring(data.getMobile(), 7);
            }
            user = new TUser();
            user.setUid(IdentifierUtils.uuid36());
            user.setName(name);
            user.setMobile(data.getMobile());
            user.setDeviceId(data.getDeviceId());
            user.setPortrait(data.getPortrait());
            user.setType(UserType.USER.getValue());
            user.setCreateDt(curDate);
            user.setUpdateDt(curDate);
            //用户信息保存至数据库
            userDao.save(user);
            log.info("==login:user:{},saved user", JSON.toJSONString(user));
            //发送Demo用户体验接口
        }
        return user;
    }

    public static void main(String[] arr) {
        long second = getRemainSecondsOneDay(new Date());
        System.out.println(second);
        System.out.println(second / 60 / 60);

        String phone = "19525488087";

        System.out.println(isMobile(phone));
    }
}
