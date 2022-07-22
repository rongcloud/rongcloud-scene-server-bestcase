package cn.rongcloud.mic.game.service.impl;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.HttpHelper;
import cn.rongcloud.common.utils.IMSignUtil;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestException;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.game.constant.GameConstant;
import cn.rongcloud.mic.game.entity.Game;
import cn.rongcloud.mic.game.entity.GameReport;
import cn.rongcloud.mic.game.entity.GameReportInfo;
import cn.rongcloud.mic.game.mapper.GameMapper;
import cn.rongcloud.mic.game.mapper.GameReportMapper;
import cn.rongcloud.mic.game.mapper.GameReportSudMapper;
import cn.rongcloud.mic.game.pojos.*;
import cn.rongcloud.mic.game.service.GameService;
import cn.rongcloud.mic.room.dao.RoomDao;
import cn.rongcloud.mic.room.mapper.RoomMapper;
import cn.rongcloud.mic.room.model.TRoom;
import cn.rongcloud.mic.room.pojos.ResRoomInfo;
import cn.rongcloud.mic.room.service.RoomService;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.service.UserService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.sud.mgp.auth.ErrorCodeEnum;
import tech.sud.mgp.auth.api.SudCode;
import tech.sud.mgp.auth.api.SudMGPAuth;
import tech.sud.mgp.auth.api.SudSSToken;
import tech.sud.mgp.auth.api.SudUid;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GameServiceImpl implements GameService {


    @Resource
    private GameMapper gameMapper;

    @Resource
    private GameReportMapper gameReportMapper;

    @Resource
    private GameReportSudMapper gameReportSudMapper;

    @Resource
    private SudMGPAuth sudMGPAuth;

    @Autowired
    private HttpHelper httpHelper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserService userService;

    @Value("${game.url}")
    private String gameUrl;

    @Value("${game.appId}")
    private String gameAppId;

    @Value("${game.appSecret}")
    private String gameAppSecret;

    @Value("${game.reportSecret}")
    private String reportSecret;

    @Value("${domainUrl}")
    private String domainUrl;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    IMSignUtil imSignUtil;

    private static final String CONTRY = "zh-CN";

    @Override
    public int save(Game game) {
        return gameMapper.insert(game);
    }

    @Override
    public List<Game> findAll() {
        return gameMapper.selectList(new QueryWrapper<Game>().eq("del_flag","0"));
    }

    @Override
    public BaseResp<GetSSTokenResp> getSSToken(String code) {
        log.info("game service getSSToken code:{}",code);
        BaseResp<GetSSTokenResp> baseResp = new BaseResp<>();

        SudUid sudUid = sudMGPAuth.getUidByCode(code);
        log.info("game service getSSToken sudUid:{}", GsonUtil.toJson(sudUid));
        if (!sudUid.isSuccess()) {
            baseResp.setRetCode(RetCodeEnum.REQUEST_FAILED);
            baseResp.setSdk_error_code(sudUid.getErrorCode());
            return baseResp;
        }
        SudSSToken sudSSToken = sudMGPAuth.getSSToken(sudUid.getUid());
        log.info("game service getSSToken sudSSToken:{}", GsonUtil.toJson(sudSSToken));
        baseResp.setData(GetSSTokenResp.builder()
                .ss_token(sudSSToken.getToken())
                .expire_date(sudSSToken.getExpireDate())
                .build());
        return baseResp;
    }

    @Override
    public BaseResp<UpdateSSTokenResp> updateSSToken(String ssToken) {
        log.info("game service updateSSToken ssToken:{}", ssToken);
        BaseResp<UpdateSSTokenResp> baseResp = new BaseResp<>();
        SudUid sudUid = sudMGPAuth.getUidBySSToken(ssToken);
        log.info("game service updateSSToken sudUid:{}", GsonUtil.toJson(sudUid));
        if (!sudUid.isSuccess()) {
            baseResp.setRetCode(RetCodeEnum.REQUEST_FAILED);
            baseResp.setSdk_error_code(sudUid.getErrorCode());
            return baseResp;
        }

        SudSSToken sudSSToken = sudMGPAuth.getSSToken(sudUid.getUid());
        log.info("game service updateSSToken sudSSToken:{}", GsonUtil.toJson(sudSSToken));
        baseResp.setData(UpdateSSTokenResp.builder()
                .ss_token(sudSSToken.getToken())
                .expire_date(sudSSToken.getExpireDate())
                .build());
        return baseResp;
    }

    @Override
    public BaseResp<GetUserInfoResp> getUserInfo(String ssToken, HttpServletRequest request) {
        log.info("game service getUserInfo ssToken:{}",ssToken);
        BaseResp<GetUserInfoResp> baseResp = new BaseResp<>();

        SudUid sudUid = sudMGPAuth.getUidBySSToken(ssToken);
        if (!sudUid.isSuccess()) {
            baseResp.setRetCode(RetCodeEnum.REQUEST_FAILED);
            baseResp.setSdk_error_code(sudUid.getErrorCode());
            return baseResp;
        }
        TUser userInfo = userService.getUserInfo(sudUid.getUid());
        log.info("game service getUserInfo userInfo:{}",GsonUtil.toJson(userInfo));
        String url = request.getScheme() + "://"  + domainUrl;
        if(StringUtils.isEmpty(userInfo.getPortrait())){
            userInfo.setPortrait(url + "/static/game/1.jpg");
        } else if(!"http".equals(userInfo.getPortrait().substring(0,4))){
            userInfo.setPortrait(url + "/file/show?path=" + userInfo.getPortrait());
        }
        // 需获取uid对应的用户信息（昵称，性别，头像地址），头像地址最优为返回128*128
        baseResp.setData(GetUserInfoResp.builder()
                .uid(sudUid.getUid())
                .nick_name(userInfo.getName())
                .gender(userInfo.getSex()==null||userInfo.getSex().intValue()==1?"male":"female")
                .avatar_url(userInfo.getPortrait())
                .build());
        return baseResp;
    }

    @Override
    public BaseResp reportGameInfo(GameReportInfo reqParam) {
        BaseResp baseResp = new BaseResp();
        ErrorCodeEnum errorCodeEnum = sudMGPAuth.verifySSToken(reqParam.getSs_token());
        if(!ErrorCodeEnum.SUCCESS.equals(errorCodeEnum)){
            baseResp.setRetCode(RetCodeEnum.REQUEST_FAILED);
            return baseResp;
        }
        try {
            gameReportSudMapper.add(reqParam);
        } catch (Exception e){
            if(e.getCause() instanceof SQLIntegrityConstraintViolationException){
                log.info("game service report game info error:重复上报, game_round_id:{}", reqParam.getReport_msg().getGame_round_id());
            } else {
                baseResp.setRetCode(RetCodeEnum.REQUEST_FAILED);
                log.info("game service report game info error data:{}", reqParam);
                e.printStackTrace();
            }
        }
        return baseResp;
    }

    @Override
    public RestResult<LoginResp> login(String userId) {
        log.info("game service login userId:{}",userId);
        // 生成短期令牌Code, 不传expireDuration参数则使用SDK中默认两小时过期
        SudCode sudCode = sudMGPAuth.getCode(userId);

        // 返回响应结果
        LoginResp build = LoginResp.builder()
                .code(sudCode.getCode())
                .expireDate(sudCode.getExpireDate())
                .build();
        return RestResult.success(build);
    }

    @Override
    public RestResult<List<GameResp>> getGameList() {
        List<GameResp> gameResps = (List<GameResp>)redisUtils.get(GameConstant.GAME_LIST_KEY);
        if(!CollectionUtils.isEmpty(gameResps)){
            return RestResult.success(gameResps);
        }
        byte[] key = gameAppSecret.getBytes();
        HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);
        String appServerSign = mac.digestHex(gameAppId);
        String url = gameUrl+appServerSign;
        try {
            HttpURLConnection getUrlConnection = httpHelper.createGetHttpConnection(url);
            String respon = httpHelper.returnResult(getUrlConnection);
            GetUrlResp urlResp = (GetUrlResp)GsonUtil.fromJson(respon, GetUrlResp.class);
            HttpURLConnection postHttpConnection = httpHelper.createPostHttpConnection(urlResp.getApi().getGet_mg_list(), "application/json");
            GetGameListReq req = new GetGameListReq();
            req.setApp_id(gameAppId);
            req.setApp_secret(gameAppSecret);
            httpHelper.setBodyParameter(GsonUtil.toJson(req), postHttpConnection);
            String response = httpHelper.returnResult(postHttpConnection);
            JSONObject jsonObject = JSONObject.parseObject(response);
            int ret_code = jsonObject.getIntValue("ret_code");
            if(ret_code!=RetCodeEnum.SUCCESS.getIndex()){
                return RestResult.generic(ret_code,jsonObject.getString("ret_msg"));
            }
            JSONObject dataJson = jsonObject.getJSONObject("data");
            String mg_info_list = dataJson.getString("mg_info_list");
            List<GameListResp> gameListResps = JSONArray.parseArray(mg_info_list, GameListResp.class);
            gameResps = Lists.newArrayList();
            for(GameListResp resp : gameListResps){
                GameResp gameResp = new GameResp();
                gameResp.setGameId(resp.getMg_id_str());
                Map<String,String> descMap = (Map) GsonUtil.fromJson(resp.getDesc(), Map.class);
                gameResp.setGameDesc(descMap.get(CONTRY));
                Map<String,String> nameMap = (Map) GsonUtil.fromJson(resp.getName(), Map.class);
                gameResp.setGameName(nameMap.get(CONTRY));
                gameResp.setGameMode(resp.getGame_mode_list().get(0).getMode());
                gameResp.setMinSeat(resp.getGame_mode_list().get(0).getCount()[0]);
                gameResp.setMaxSeat(resp.getGame_mode_list().get(0).getCount()[1]);
                Map<String,String> loadingMap = (Map) GsonUtil.fromJson(resp.getBig_loading_pic(), Map.class);
                gameResp.setLoadingPic(loadingMap.get(CONTRY));
                Map<String,String> thumbnailMap = (Map) GsonUtil.fromJson(resp.getThumbnail332x332(), Map.class);
                gameResp.setThumbnail(thumbnailMap.get(CONTRY));
                gameResps.add(gameResp);
            }

            redisUtils.set(GameConstant.GAME_LIST_KEY,gameResps,GameConstant.GAME_LIST_TIME);
            return RestResult.success(gameResps);
        } catch (Exception e) {
            log.error("game list error message:{}",e.getMessage(),e);
        }
        return RestResult.success();
    }

    @Override
    public RestResult report(GameReportReq reqParam, Long signTimestamp, String nonce, String signature) {
        if(reqParam==null|| StringUtils.isEmpty(reqParam.getAppId())||StringUtils.isEmpty(reqParam.getGameId())){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        if(!imSignUtil.validSign(reportSecret,nonce, signTimestamp.toString(), signature)){
            log.info("report Access denied: signature error, signTimestamp:{}, nonce:{}, signature:{}", signTimestamp, nonce, signature);
            return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
        }
        GameReport gameReport = new GameReport();
        BeanUtils.copyProperties(reqParam,gameReport);
        gameReportMapper.insert(gameReport);
        return RestResult.success();
    }

    @Override
    public RestResult<ResRoomInfo> join(String gameId, JwtUser jwtUser) throws Exception {
        TRoom tRoom = roomMapper.selectOneByGameId(gameId);
        if(tRoom != null){
            return RestResult.success(roomService.build(tRoom));
        }
        return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
    }

    @Override
    public RestResult toggleGameStatus(GameStatusReq gameStatusReq) throws RestException {
        TRoom room = roomService.getRoomInfo(gameStatusReq.getRoomId());
        if (!gameStatusReq.getGameStatus().equals(room.getGameStatus())) {
            room.setGameStatus(gameStatusReq.getGameStatus());
            roomDao.updateGameIdAndGameStatus(gameStatusReq.getRoomId(), room.getGameId(), gameStatusReq.getGameStatus());
            roomService.updateRoomCache(room);
        }
        return RestResult.success();
    }

    @Override
    public RestResult toggleGameId(GameStatusReq gameStatusReq, JwtUser jwtUser) throws RestException {
        TRoom room = roomService.getRoomInfo(gameStatusReq.getRoomId());
        if (room == null){
            throw new RestException(RestResultCode.ERR_ROOM_NOT_EXIST.getCode(), RestResultCode.ERR_ROOM_NOT_EXIST.getMsg());
        }
        roomService.roomManageUserCheck(room, jwtUser.getUserId());
        if (!gameStatusReq.getGameId().equals(room.getGameId())) {
            room.setGameId(gameStatusReq.getGameId());
            room.setGameStatus(1);
            roomDao.updateGameIdAndGameStatus(gameStatusReq.getRoomId(), gameStatusReq.getGameId(), 1);
            roomService.updateRoomCache(room);
        }
        return RestResult.success();
    }
}
