package cn.rongcloud.mic.room.service;

import cn.rongcloud.common.im.ChrmEntrySetInfo;
import cn.rongcloud.common.im.IMHelper;
import cn.rongcloud.common.im.pojos.*;
import cn.rongcloud.common.utils.DateTimeUtils;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.IMSignUtil;
import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.mic.common.constant.CustomerConstant;
import cn.rongcloud.mic.common.exception.BusinessException;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestException;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.game.enums.GameStatusEnum;
import cn.rongcloud.mic.game.pojos.GameResp;
import cn.rongcloud.mic.game.service.GameService;
import cn.rongcloud.mic.room.constant.RoomKeysCons;
import cn.rongcloud.mic.room.dao.RoomDao;
import cn.rongcloud.mic.room.dao.RoomMusicDao;
import cn.rongcloud.mic.room.enums.*;
import cn.rongcloud.mic.room.key.KeyGen;
import cn.rongcloud.mic.room.mapper.RoomMapper;
import cn.rongcloud.mic.room.message.ChrmKeepAliveMessage;
import cn.rongcloud.mic.room.message.ChrmPkMessage;
import cn.rongcloud.mic.room.model.TRoom;
import cn.rongcloud.mic.room.model.TTaskMic;
import cn.rongcloud.mic.room.pojos.*;
import cn.rongcloud.mic.shumei.service.ShumeiService;
import cn.rongcloud.mic.user.enums.UserSexEnum;
import cn.rongcloud.mic.user.enums.UserType;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.pojos.ReqUserIds;
import cn.rongcloud.mic.user.pojos.ResFollowInfo;
import cn.rongcloud.mic.user.pojos.ResFollowInfoExt;
import cn.rongcloud.mic.user.service.UserService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private RoomMusicDao roomMusicDao;

    @Autowired
    private UserService userService;

    @Value("${domainUrl}")
    private String domainUrl;

    @Autowired
    IMHelper imHelper;

    @Autowired
    IMSignUtil imSignUtil;

    @Autowired
    FileService fileService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    RoomPkService roomPkService;

    @Lazy
    @Autowired
    GameService gameService;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "roomRedisTemplate")
    private HashOperations<String, String, TRoom> roomHashOperations;

    @Resource(name = "roomRedisTemplate")
    private HashOperations<String, String, Object> redisHashOperations;

    @Resource(name = "roomRedisTemplate")
    private HashOperations<String, String, HashMap<String, Long>> roomHashGifts;

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    @Value("${rongrtc.domain}")
    private String domain;

    @Value("${rongrtc.mic.transfer_host_expire}")
    private Long transferHostExpire;

    @Value("${rongrtc.mic.takeover_host_expire}")
    private Long takeoverHostExpire;

    @Value("${rongrtc.room.expire}")
    private Integer roomExpire;

    private String serverName = "apiv1-rcrtc.cn.rongcloud.cn";

    @Autowired
    private ShumeiService shumeiService;

    @Autowired
    private TaskMicService taskMicService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public RestResult createRoom(ReqRoomCreate data, JwtUser jwtUser) throws Exception {
        //只有注册用户可操作
        if (!jwtUser.getType().equals(UserType.USER.getValue())) {
            return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
        }
        //判断用户是否存在
        TUser tUser = userService.getUserInfo(jwtUser.getUserId());
        if (tUser == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
        }

        String key = CustomerConstant.SERVICENAME + "|LOCK:ROOM:CREATE"+jwtUser.getUserId()+"|"+data.getName();
        RLock lock = redissonClient.getLock(key);
        boolean b = lock.tryLock(3, 3, TimeUnit.SECONDS);
        if(!b){
            return RestResult.generic(10001,"重复提交");
        }

        TRoom roomInfo = roomDao.findTRoomByUserIdAndRoomTypeEquals(jwtUser.getUserId(),data.getRoomType());
        if (roomInfo != null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_IS_EXIST, build(roomInfo));
        }


        // 审核 主题图片和房间名称
        if(!StringUtils.isBlank(data.getThemePictureUrl())){
            RestResult restResult = shumeiService.imageAudit(data.getThemePictureUrl());
            if(restResult.getCode()!=RestResultCode.ERR_SUCCESS.getCode()){
                return restResult;
            }
        }
        RestResult restResult = shumeiService.textAudit(data.getName());
        if(restResult.getCode()!=RestResultCode.ERR_SUCCESS.getCode()){
            return restResult;
        }

        //创建 IM 聊天室
        String chatRoomId = IdentifierUtils.uuid();
        IMApiResultInfo resultInfo = imHelper.createChatRoom(chatRoomId, data.getName());
        if (!resultInfo.isSuccess()) {
            log.error("create chatRoom error: {}, {}", jwtUser, resultInfo.getErrorMessage());
            return RestResult.generic(RestResultCode.ERR_ROOM_CREATE_ROOM_ERROR, resultInfo.getErrorMessage());
        }

        //将房间信息保存至数据库
        Date date = DateTimeUtils.currentUTC();
        TRoom room = new TRoom();
        room.setUid(chatRoomId);
        room.setName(data.getName());
        room.setBackgroundUrl(data.getBackgroundUrl());
        room.setThemePictureUrl(data.getThemePictureUrl());
        room.setType(RoomType.CUSTOM.getValue());
        room.setAllowedJoinRoom(true);
        room.setAllowedFreeJoinMic(true);
        room.setIsPrivate(data.getIsPrivate());
        room.setPassword(data.getPassword());
        room.setUserId(jwtUser.getUserId());
        room.setCreateDt(date);
        room.setUpdateDt(date);
        //房间类型：1聊天室 2电台
//        if (room.getRoomType() == null) {
//            room.setRoomType(1);
//        } else {
//            room.setRoomType(data.getRoomType());
//        }
        room.setRoomType(data.getRoomType());
        room.setGameId(data.getGameId());
        room.setGameStatus(GameStatusEnum.READY.getValue());
        roomDao.save(room);

        //kv 保存IM server
        initMicRoomKv(chatRoomId, data.getKv(), jwtUser.getUserId());

        //将房间信息缓存至 Redis Hash
        updateRoomCache(room);

        //将房间 Id 缓存至 Redis Zset，房间分页查询数据源
        zSetOperations.add(getRedisRoomIdsKey(), chatRoomId, date.getTime());
        zSetOperations.add(getRedisRoomIdsKey(data.getRoomType()), chatRoomId, date.getTime());
        return RestResult.success(build(room));
    }

    @Override
    public RestResult<ResRoomInfo> getRoomDetail(String roomId) {
        TRoom room = getRoomInfo(roomId);
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        return RestResult.success(build(room));
    }

    @Override
    public RestResult<ResRoomList> getRoomList(Integer type, Integer page, Integer size, HttpServletRequest request) {

        Long from = 0L;
        if (page < 1) {
            page = 1;
        }
        int start = (page - 1) * size;
        from = Long.valueOf(start);

        ResRoomList resRoomList = new ResRoomList();
        //从 redis 查询房间总数
        Long totalCount = zSetOperations.size(getRedisRoomIdsKey(type));
        resRoomList.setTotalCount(totalCount);

        String path = request.getScheme() + "://" + domainUrl;
        List<String> images = new ArrayList<>();
        images.add(path + "/static/room/1.gif");
        images.add(path + "/static/room/2.gif");
        images.add(path + "/static/room/3.jpg");
        images.add(path + "/static/room/4.jpg");
        resRoomList.setImages(images);

        List<ResRoomInfo> roomInfos = new ArrayList<>();
        //从 redis 分页查询
        Set<String> roomIds = zSetOperations.reverseRange(getRedisRoomIdsKey(type), from, (from + size - 1));
        if (roomIds == null || roomIds.isEmpty()) {
            resRoomList.setRooms(roomInfos);
            return RestResult.success(resRoomList);
        }

        //批量从 Redis 查询房间数据
        List<TRoom> list = this.getList(new ArrayList<>(roomIds));
        if (list != null && list.size() > 0) {
            for (TRoom room : list) {
                roomInfos.add(build(room));
            }
        }
        resRoomList.setRooms(roomInfos);

        return RestResult.success(resRoomList);
    }

    private List<TRoom> getList(List<String> roomIds) {
        List<TRoom> list = new ArrayList<>();
        Map<String, TRoom> roomMap = hmget(getRedisRoomInfosKey(), roomIds);
        for (String roomId : roomIds) {
            if (roomMap.containsKey(roomId)) {
                list.add(roomMap.get(roomId));
            } else {
                TRoom tRoom = this.getRoomInfo(roomId);
                if (tRoom != null) {
                    list.add(tRoom);
                }
            }
        }
        return list;
    }

    @Override
    public List<TRoom> getRoomListAll() {
        //从 redis 分页查询
        Long size = zSetOperations.size(getRedisRoomIdsKey());
        Set<String> roomIds = zSetOperations.reverseRange(getRedisRoomIdsKey(), 0, size);
        if (roomIds == null || roomIds.isEmpty()) {
            return null;
        }

        //批量从 Redis 查询房间数据
        List<TRoom> list = new ArrayList<>();
        Map<String, TRoom> roomMap = hmget(getRedisRoomInfosKey(), new ArrayList<>(roomIds));
        for (String roomId : roomIds) {
            if (roomMap.containsKey(roomId)) {
                list.add(roomMap.get(roomId));
            }
        }
        return list;
    }

    @Override
    public RestResult roomSetting(ReqRoomSetting data, JwtUser jwtUser) {
        //检查房间是否存在
        TRoom room = getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        room.setApplyAllLockMic(data.getApplyAllLockMic());
        room.setApplyAllLockSeat(data.getApplyAllLockSeat());
        room.setApplyOnMic(data.getApplyOnMic());
        room.setSetMute(data.getSetMute());
        room.setSetSeatNumber(data.getSetSeatNumber());
        //更新redis
        updateRoomCache(room);

        //是否需要更新数据库
//        boolean needUpdate = false;

        //设置房间是否允许观众加入
//        if (data.getAllowedJoinRoom() != null && room.isAllowedJoinRoom() != data.getAllowedJoinRoom()) {
//            room.setAllowedJoinRoom(data.getAllowedJoinRoom());
//            needUpdate = true;
//        }
//        //设置是否允许观众自由上麦
//        if (data.getAllowedFreeJoinMic() != null && room.isAllowedFreeJoinMic() != data.getAllowedFreeJoinMic()) {
//            room.setAllowedFreeJoinMic(data.getAllowedFreeJoinMic());
//            needUpdate = true;
//        }

//        if (needUpdate) {
//            //更新数据库
//            roomDao.updateRoomSetting(room.getUid(), room.isAllowedJoinRoom(), room.isAllowedFreeJoinMic());
//            //更新 Redis
//            updateRoomCache(room);
//        }

        return RestResult.success();
    }


    @Override
    public RestResult<ReqRoomSetting> getRoomSetting(String roomId, JwtUser jwtUser) {
        if (StringUtils.isBlank(roomId)) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }

        //检查房间是否存在
        TRoom room = getRoomInfo(roomId);
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }

        ReqRoomSetting response = new ReqRoomSetting();
        response.setRoomId(room.getUid());
        response.setSetMute(room.getSetMute());
        response.setApplyAllLockMic(room.getApplyAllLockMic());
        response.setSetSeatNumber(room.getSetSeatNumber());
        response.setApplyAllLockSeat(room.getApplyAllLockSeat());
        response.setApplyOnMic(room.getApplyOnMic());

        return RestResult.success(response);
    }

    @Override
    public RestResult roomPrivate(ReqRoomPrivate data, JwtUser jwtUser) {
        //检查房间是否存在
        TRoom room = getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }

        room.setIsPrivate(data.getIsPrivate());
        room.setPassword(data.getPassword());
        //更新数据库
        roomDao.updateRoomPrivate(room.getUid(), room.getIsPrivate(), room.getPassword());
        //更新 Redis
        updateRoomCache(room);

        return RestResult.success();
    }

    @Override
    public RestResult roomName(ReqRoomName data, JwtUser jwtUser) {
        //检查房间是否存在
        TRoom room = getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }

        RestResult restResult = shumeiService.textAudit(data.getName());
        if(restResult.getCode()!=RestResultCode.ERR_SUCCESS.getCode()){
            return restResult;
        }
        room.setName(data.getName());
        //更新数据库
        roomDao.updateRoomName(room.getUid(), room.getName());
        //更新 Redis
        updateRoomCache(room);

        return RestResult.success();
    }

    @Override
    public RestResult roomManage(ReqRoomMicManage data, JwtUser jwtUser) throws Exception {
        //检查房间是否存在
        TRoom room = getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        //检查用户是否在房间
        if (!isInChrm(data.getRoomId(), data.getUserId())) {
            return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_IN);
        }
        if (!data.getIsManage()) {
            setOperations.remove(getRedisRoomManageUserKey(data.getRoomId()), data.getUserId());
        } else {
            setOperations.add(getRedisRoomManageUserKey(data.getRoomId()), data.getUserId());
        }
        return RestResult.success();
    }

    @Override
    public RestResult roomManageList(String roomId, JwtUser jwtUser) {
        //检查房间是否存在
        TRoom room = getRoomInfo(roomId);
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        List<RespRoomUser> outs = new ArrayList<>();
        //查询管理员列表
        Set<String> manageUserIds = setOperations.members(getRedisRoomManageUserKey(roomId));
        if (manageUserIds == null || manageUserIds.isEmpty()) {
            return RestResult.success(outs);
        }

        for (String manageUserId : manageUserIds) {
            RespRoomUser out = new RespRoomUser();
            TUser user = userService.getUserInfo(manageUserId);
            if (user != null) {
                out.setUserId(user.getUid());
                out.setUserName(user.getName());
                out.setPortrait(user.getPortrait());
                outs.add(out);
            }
        }

        return RestResult.success(outs);
    }

    @Override
    public RestResult chrmDelete(String roomId) throws Exception {
        //检查房间是否存在
        TRoom room = getRoomInfo(roomId);
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        //删除服务器房间
        List<String> deleteRoomIds = new ArrayList<String>() {{
            add(roomId);
        }};

        // pk need  清pk
        roomPkService.cleanCacheByStopPk(roomId);

        //20210712 添加 直接删除
        //调用删除房间
        ReqRoomStatusSync status = new ReqRoomStatusSync();
        status.setChatRoomId(room.getUid());
        status.setType(3);
        this.dealChrm(room, status);

        IMApiResultInfo resultInfo = imHelper.deleteChrm(deleteRoomIds);
//        if (!resultInfo.isSuccess()) {
//            return RestResult.generic(RestResultCode.ERR_ROOM_ADD_GAG_USER_ERROR);
//        }

        return RestResult.success();
    }

    @Override
    public RestResult roomBackground(ReqRoomBackground data, JwtUser jwtUser) {
        //检查房间是否存在
        TRoom room = getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        room.setBackgroundUrl(data.getBackgroundUrl());
        //更新数据库
        roomDao.updateRoomBackground(room.getUid(), room.getBackgroundUrl());
        //更新 Redis
        updateRoomCache(room);
        return RestResult.success();
    }

    @Override
    public RestResult roomCreateCheck(JwtUser jwtUser,Integer roomType) {
        if(roomType==null){
            List<TRoom> dbRooms = roomDao.findTRoomByUserIdEquals(jwtUser.getUserId());
            if(!CollectionUtils.isEmpty(dbRooms)){
                return RestResult.generic(RestResultCode.ERR_ROOM_IS_EXIST, build(dbRooms.get(0)));
            }
        }else{
            TRoom roomInfo = roomDao.findTRoomByUserIdAndRoomTypeEquals(jwtUser.getUserId(),roomType);
            if (roomInfo != null) {
                return RestResult.generic(RestResultCode.ERR_ROOM_IS_EXIST, build(roomInfo));
            }
        }

        return RestResult.success();
    }

    @Override
    public RestResult addGift(ReqRoomGiftAdd data, JwtUser jwtUser) {
        TRoom room = getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        HashMap<String, Long> userGiftTotalValueList = new HashMap<>();
        //获取当前的礼物总价值
        Long currentValue = Long.valueOf(ChartRoomGift.getChartRoomGiftById(data.getGiftId()).getValue() * data.getNum());
        //pk状态记录
        roomPkService.pkGift(data.getRoomId(), jwtUser.getUserId(),data.getToUid(), currentValue);

        HashMap<String, Long> userTotalValue;
        userTotalValue = roomHashGifts.get(getRedisRoomGiftKey(data.getRoomId()), data.getToUid());
        log.info("gift log data:{}", userTotalValue);
        if (userTotalValue != null) {
            currentValue = currentValue + userTotalValue.get(data.getToUid());
        }
        userGiftTotalValueList.put(data.getToUid(), currentValue);
        roomHashGifts.put(getRedisRoomGiftKey(data.getRoomId()), data.getToUid(), userGiftTotalValueList);

        return RestResult.success();
    }


    @Override
    public RestResult testPush(String roomId) throws Exception {
        String uid = "0143ad2a-5fb6-4c8f-a37c-744460ff66cd";
//        String roomId = "DXPbZWUdSG0ug8OFKRlBLY";
        ChrmPkMessage pkMessage = new ChrmPkMessage();
        pkMessage.setRoomId(roomId);
        pkMessage.setScore(90L);
        String[] sendRoomIdList = {roomId, roomId};

        //IMApiResultInfo resultInfo = imHelper.publishMessage(uid, sendRoomIdList, pkMessage);
        //pkGift(roomId, uid, 80L);
        ReqRoomGiftAdd reqRoomGiftAdd = new ReqRoomGiftAdd();
        reqRoomGiftAdd.setRoomId(roomId);
        reqRoomGiftAdd.setToUid(uid);
        reqRoomGiftAdd.setGiftId(1);
        reqRoomGiftAdd.setNum(10);

        JwtUser user = new JwtUser();
        user.setUserId(uid);
        user.setUserName("zhangsan");
        user.setType(1);
        addGift(reqRoomGiftAdd, user);
        return RestResult.success();
    }



    @Override
    public RestResult giftList(String roomId, JwtUser jwtUser) {
        TRoom room = getRoomInfo(roomId);
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        List<HashMap<String, Long>> list = roomHashGifts.values(getRedisRoomGiftKey(roomId));
        return RestResult.success(list);
    }

    @Override
    public void removeRoom(TRoom room) throws Exception {
        if (room == null) {
            return;
        }
        log.info("[removeRoom][uid:" + room.getUid() + "][updateDate:" + room.getUpdateDt() + "]");
        //判断是否到达3个小时
        if (DateUtils.addHours(room.getUpdateDt(), roomExpire).compareTo(new Date()) > 0) {
            return;
        }
        //从im服务器重新获取房间多少人员
        RestResult<List<RespRoomUser>> roomMembers = this.getRoomMembers(room.getUid());
        Integer total = 0;
        if(roomMembers.isSuccess() && !CollectionUtils.isEmpty(roomMembers.getResult())){
            total = roomMembers.getResult().size();
        }

        log.info("[removeRoom][uid:" + room.getUid() + "][member_total:" + total + "]");
        if (total.equals(0)) {
            //删除 房间
            this.chrmDelete(room.getUid());
        }
    }

    @Override
    public RestResult roomUserGag(ReqRoomUserGag data, JwtUser jwtUser) throws Exception {

        if (data.getUserIds().isEmpty()) {
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        //每次最多只能设置 20 个用户
        if (data.getUserIds().size() > 20) {
            return RestResult.generic(RestResultCode.ERR_ROOM_USER_IDS_SIZE_EXCEED);
        }
        //检查房间是否存在
        TRoom room = getRoomInfo(data.getRoomId());
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        //IM 设置用户禁言
        IMApiResultInfo resultInfo;
        if (data.getOperation().equals(RoomUserGagOperation.ADD.getValue())) {
            resultInfo = imHelper.addGagChatroomUser(data.getRoomId(), data.getUserIds(), null);
        } else if (data.getOperation().equals(RoomUserGagOperation.REMOVE.getValue())) {
            resultInfo = imHelper.removeGagChatroomUser(data.getRoomId(), data.getUserIds());
        } else {
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        if (!resultInfo.isSuccess()) {
            return RestResult.generic(RestResultCode.ERR_ROOM_ADD_GAG_USER_ERROR);
        }

        //将禁言用户信息保存至 Redis
        if (data.getOperation().equals(RoomUserGagOperation.ADD.getValue())) {
            setOperations.add(getRedisRoomGagUserKey(data.getRoomId()), data.getUserIds().toArray(new String[0]));
        } else if (data.getOperation().equals(RoomUserGagOperation.REMOVE.getValue())) {
            setOperations.remove(getRedisRoomGagUserKey(data.getRoomId()), data.getUserIds().toArray(new String[0]));
        }

        return RestResult.success();
    }

    @Override
    public RestResult<List<RespRoomUser>> getRoomMembers(String roomId) throws Exception {
        List<RespRoomUser> outs = new ArrayList<>();
        //检查房间是否存在
        TRoom room = getRoomInfo(roomId);
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        //TODO: 如果您正在使用开发环境的 AppKey，您的应用只能注册 100 名用户，达到上限后，将返回错误码 2007.
        // 如果您需要更多的测试账户数量，您需要在应用配置中申请“增加测试人数”。
        // 如果是生成环境记得 改成分页 重要、重要、重要。。。。
        IMChatRoomUserResult result = imHelper.queryChatroomUser(roomId, 100, 1);
        if (result != null && result.isSuccess() && result.getUsers() != null && result.getUsers().size() > 0) {
            List<IMRoomUserInfo> userInfos = result.getUsers();
            for (IMRoomUserInfo roomUserInfo : userInfos) {
                TUser user = userService.getUserInfo(roomUserInfo.getId());
                if (user != null) {
                    RespRoomUser out = new RespRoomUser();
                    out.setUserId(user.getUid());
                    out.setUserName(user.getName());
                    out.setPortrait(user.getPortrait());
                    outs.add(out);
                }
            }
        }

        return RestResult.success(outs);
    }

    @Override
    public RestResult getRoomMembersV2(String roomId, JwtUser jwtUser) throws Exception {
        RestResult restResult = getRoomMembers(roomId);
        if (restResult.isSuccess()) {
            List<RespRoomUser> outs = (List<RespRoomUser>) restResult.getResult();
            if (outs != null && !outs.isEmpty()) {
                List<String> userIds = outs.stream().map(RespRoomUser::getUserId).collect(Collectors.toList());
                ReqUserIds reqUserIds = new ReqUserIds();
                reqUserIds.setUserIds(userIds);
                RestResult userResult = userService.batchGetUsersInfo(reqUserIds, jwtUser);
                List<ResFollowInfo> resFollowInfos = (List<ResFollowInfo>) userResult.getResult();
                List<ResFollowInfoExt> theUserDetails = new ArrayList<>();
                RestResult managerResult = this.roomManageList(roomId, jwtUser);
                if (managerResult.isSuccess()) {
                    List<RespRoomUser> roomManagerUsers = (List<RespRoomUser>) managerResult.getResult();
                    if (roomManagerUsers != null && !roomManagerUsers.isEmpty()) {
                        List<String> managerUids = roomManagerUsers.stream().map(RespRoomUser::getUserId).collect(Collectors.toList());
                        for (ResFollowInfo res : resFollowInfos) {
                            ResFollowInfoExt ext = new ResFollowInfoExt();
                            BeanUtils.copyProperties(res, ext);
                            if (managerUids.contains(ext.getUserId())) {
                                ext.setAdmin(true);
                            }
                            theUserDetails.add(ext);
                        }
                        return RestResult.success(theUserDetails);
                    } else {
                        return RestResult.success(resFollowInfos);
                    }
                }
            }
        }
        return RestResult.generic(RestResultCode.ERR_OTHER, "empty result");
    }

    @Override
    public RestResult<List<RespRoomUser>> queryGagRoomUsers(String roomId, JwtUser jwtUser) {

        List<RespRoomUser> outs = new ArrayList<>();

        //检查房间是否存在
        TRoom room = getRoomInfo(roomId);
        if (room == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }

        //查询禁言用户
        Set<String> gagUserIds = setOperations.members(getRedisRoomGagUserKey(roomId));
        if (gagUserIds == null || gagUserIds.isEmpty()) {
            return RestResult.success(outs);
        }

        for (String gagUserId : gagUserIds) {
            RespRoomUser out = new RespRoomUser();
            TUser user = userService.getUserInfo(gagUserId);
            if (user != null) {
                out.setUserId(user.getUid());
                out.setUserName(user.getName());
                out.setPortrait(user.getPortrait());
                outs.add(out);
            }
        }

        return RestResult.success(outs);
    }

    @Override
    public RestResult toggleRoomType(ReqRoomType reqRoomType, JwtUser jwtUser) {
        RestResult result = this.checkData(reqRoomType.getRoomId(), jwtUser);
        if (!result.isSuccess()) {
            return result;
        }
        TRoom roomInfo = (TRoom) result.getResult();
        //不相同才切换，相同就不用切换
        if (!reqRoomType.getRoomType().equals(roomInfo.getRoomType())) {
            roomInfo.setRoomType(reqRoomType.getRoomType());
            roomDao.updateRoomType(reqRoomType.getRoomId(), roomInfo.getRoomType());
            updateRoomCache(roomInfo);
        }
        return RestResult.success();
    }

    @Override
    public RestResult stopRoom(String roomId, JwtUser jwtUser) {
        RestResult result = this.checkData(roomId, jwtUser);
        if (!result.isSuccess()) {
            return result;
        }
        TRoom roomInfo = (TRoom) result.getResult();
        Date currentDate = new Date();
        //暂停5分钟
        roomInfo.setStopEndTime(DateUtils.addMinutes(currentDate, 5));
        roomDao.updateStopEndTime(roomId, roomInfo.getStopEndTime());
        updateRoomCache(roomInfo);
        return RestResult.success();
    }



    @Override
    public RestResult<List<ResRoomInfo>> onlineCreatedList(JwtUser jwtUser,Integer roomType) {

        TRoom tRoom = null;
        if(roomType==null){
            List<TRoom> dbRooms = roomDao.findTRoomByUserIdEquals(jwtUser.getUserId());
            if(CollectionUtils.isEmpty(dbRooms)){
                return RestResult.generic(RestResultCode.ERR_USER_NOT_ROOM);
            }
            tRoom = dbRooms.get(0);
        }else{
            tRoom = roomDao.findTRoomByUserIdAndRoomTypeEquals(jwtUser.getUserId(),roomType);
            if(tRoom==null){
                return RestResult.generic(RestResultCode.ERR_USER_NOT_ROOM);
            }
        }

        List<String> onlineList = roomDao.findOnlineList(tRoom.getRoomType(), tRoom.getUid());
        List<ResRoomInfo> list = new ArrayList<>();
        //批量从 Redis 查询房间数据
        List<TRoom> roomList = this.getList(onlineList);
        if (roomList != null && roomList.size() > 0) {
            for (TRoom tRoom1 : roomList) {
                list.add(this.build(tRoom1));
            }
        }
        return RestResult.success(list);
    }



    @Override
    public RestResult startRoom(String roomId, JwtUser jwtUser) {
        RestResult result = this.checkData(roomId, jwtUser);
        if (!result.isSuccess()) {
            return result;
        }
        TRoom roomInfo = (TRoom) result.getResult();
        roomInfo.setStopEndTime(null);
        roomDao.updateStopEndTime(roomId, null);
        updateRoomCache(roomInfo);
        return RestResult.success();
    }

    @Override
    public RestResult checkData(String roomId, JwtUser jwtUser) {
        //判断用户是否存在
        TUser tUser = userService.getUserInfo(jwtUser.getUserId());
        if (tUser == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
        }

        TRoom roomInfo = this.getRoomInfo(roomId);
        if (roomInfo == null) {
            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
        }
        //只有房主才能暂停房间
        if (!tUser.getUid().equals(roomInfo.getUserId())) {
            return RestResult.generic(RestResultCode.ERR_NOT_AUTH);
        }

        return RestResult.success(roomInfo);
    }

    @Async
    @Override
    public void chrmStatusSync(List<ReqRoomStatusSync> data, Long signTimestamp, String nonce, String signature) {

        //校验签名是否正确
        if (!imSignUtil.validSign(nonce, signTimestamp.toString(), signature)) {
            log.info("Access denied: signature error, signTimestamp:{}, nonce:{}, signature:{}", signTimestamp, nonce, signature);
            return;
        }

        if (data == null || data.isEmpty()) {
            return;
        }

        //同步聊天室状态
        for (ReqRoomStatusSync status : data) {
            TRoom room = getRoomInfo(status.getChatRoomId());
            if (room == null) {
                continue;
            }
            dealChrm(room, status);
        }
    }


    private void dealChrm(TRoom room, ReqRoomStatusSync status) {
        if (status.getType() == ChartRoomSyncType.DESTORY.getValue()) {
            if (room.getType() == RoomType.OFFICIAL.getValue()) {
                //重置房间属性
                room.setAllowedJoinRoom(true);
                room.setAllowedFreeJoinMic(true);
                roomDao.updateRoomSetting(status.getChatRoomId(), true, false);
                updateRoomCache(room);

            } else if (room.getType() == RoomType.CUSTOM.getValue()) {
                //从缓存中删除房间对应的 Key
                redisTemplate.opsForHash().delete(getRedisRoomInfosKey(), status.getChatRoomId());
                redisTemplate.opsForZSet().remove(getRedisRoomIdsKey(), status.getChatRoomId());
                redisTemplate.opsForZSet().remove(getRedisRoomIdsKey(room.getRoomType()), status.getChatRoomId());
                //从数据库中删除房间
                roomDao.deleteTRoomByUidEquals(room.getUid());
            }
            //删除房间用户统计
            redisHashOperations.delete(REDIS_ROOM_USER_TOTAL_KEY, status.getChatRoomId());
            //删除禁言用户列表
            redisTemplate.delete(getRedisRoomGagUserKey(status.getChatRoomId()));
            //删除管理员列表
            redisTemplate.delete(getRedisRoomManageUserKey(status.getChatRoomId()));
            //删除礼物列表
            redisUtils.del(getRedisRoomGiftKey(status.getChatRoomId()));
        } else if (status.getType() == ChartRoomSyncType.JOIN.getValue()) {
            //加入聊天室
            this.updateRoomUserTotal(status.getChatRoomId(), status.getUserIds() == null ? 0 : status.getUserIds().size());
            //同步更新redis 房间修改时间
            this.syncRoomUpdate(room);
        } else if (status.getType() == ChartRoomSyncType.QUIT.getValue()) {
            //退出聊天室
            this.updateRoomUserTotal(status.getChatRoomId(), status.getUserIds() == null ? 0 : -status.getUserIds().size());
            //同步更新redis 房间修改时间
            this.syncRoomUpdate(room);
        }
    }

    private void syncRoomUpdate(TRoom room) {
        room.setUpdateDt(new Date());
        this.updateRoomCache(room);
    }

    @Override
    public RestResult messageBroadcast(ReqBroadcastMessage data, JwtUser jwtUser)
            throws Exception {
        if (StringUtils.isEmpty(data.getFromUserId())) {
            data.setFromUserId(CustomerConstant.SYSTEM_USER_ID);
        }

        imHelper.publishBroadcastMessage(data.getFromUserId(), data.getObjectName(), data.getContent());

        return RestResult.success();
    }

    /**
     * 聊天室创建时，向 IM 聊天室初始化端上传的kv
     *
     * @param roomId 聊天室 ID
     * @throws Exception
     */
    private void initMicRoomKv(String roomId, List<Map<String, String>> kvs, String userId) throws Exception {
        log.info("kv info {}", GsonUtil.toJson(kvs));
        if(CollectionUtils.isEmpty(kvs)){
            return;
        }
        for (Map<String, String> item : kvs) {
            if(item.isEmpty()){
                return;
            }
            String kvKey = item.get("key");
            String kvValue = item.get("value");
            log.info("create room, key:{}, value:{}", kvKey, kvValue);
            ChrmEntrySetInfo entrySetInfo = new ChrmEntrySetInfo();
            entrySetInfo.setChrmId(roomId);
            entrySetInfo.setUserId(userId);
            entrySetInfo.setKey(kvKey);
            entrySetInfo.setValue(kvValue);
            entrySetInfo.setAutoDelete(false);
            imHelper.entrySet(entrySetInfo, 5);
        }
    }

    //============= Room 相关 ==================
    private boolean isInChrm(String roomId, String userId) throws Exception {
        // 从 IM 聊天室查询用户是否存在
        IMIsInChrmResult result = imHelper.isInChartRoom(roomId, userId);
        return result.isSuccess() && result.getIsInChrm();
    }

    @Override
    public void updateRoomCache(TRoom room) {
        roomHashOperations.put(getRedisRoomInfosKey(), room.getUid(), room);
    }

    /**
     * @description: 删除房间redis
     * @date: 2021/6/2 10:14
     * @author: by zxw
     **/
    private void removeRoomCache(String roomId) {
        roomHashOperations.delete(getRedisRoomInfosKey(), roomId);
    }

    @Override
    public TRoom getRoomInfo(String roomId) {
        //先从缓存查询，不存在则从数据库查询
        TRoom room = roomHashOperations.get(getRedisRoomInfosKey(), roomId);
        if (room != null) {
            return room;
        }
        log.info("get room info from db, roomId:{}", roomId);
        room = roomDao.findTRoomByUidEquals(roomId);
        if (room != null) {
            updateRoomCache(room);
        }
        return room;
    }

    @Override
    public RestResult<ResRoomList> getRoomPage(Integer roomType, Integer sex, String gameId, Integer page, Integer size, HttpServletRequest request) {

        if (page < 1) {
            page = 1;
        }
        ResRoomList resRoomList = new ResRoomList();
        long total = roomMapper.findRoomPageTotal(roomType, sex, gameId);
        resRoomList.setTotalCount(total);

        String path = request.getScheme() + "://" + domainUrl;
        List<String> images = new ArrayList<>();
        images.add(path + "/static/room/1.gif");
        images.add(path + "/static/room/2.gif");
        images.add(path + "/static/room/3.jpg");
        images.add(path + "/static/room/4.jpg");
        resRoomList.setImages(images);

        List<TRoom> dbRooms = roomMapper.findRoomPage(roomType, sex, gameId, (page-1)*size, page*size);

        List<ResRoomInfo> roomInfos = Lists.newArrayList();
        if (dbRooms != null && dbRooms.size() > 0) {
            for (TRoom room : dbRooms) {
                roomInfos.add(build(room));
            }
        }
        resRoomList.setRooms(roomInfos);

        return RestResult.success(resRoomList);
    }


    public Map<String, TRoom> hmget(String key, List<String> fields) {
        List<TRoom> result = roomHashOperations.multiGet(key, fields);
        Map<String, TRoom> ans = new HashMap<>(fields.size());
        int index = 0;
        for (String field : fields) {
            if (result.get(index) == null) {
                continue;
            }
            ans.put(field, result.get(index));
            index++;
        }
        return ans;
    }

    @Override
    public ResRoomInfo build(TRoom room) {
        ResRoomInfo roomInfo = new ResRoomInfo();
        roomInfo.setId(room.getId() + 10000);
        roomInfo.setRoomId(room.getUid());
        roomInfo.setRoomName(room.getName());
        roomInfo.setThemePictureUrl(room.getThemePictureUrl());
        roomInfo.setBackgroundUrl(room.getBackgroundUrl());
        roomInfo.setIsPrivate(room.getIsPrivate());
        roomInfo.setUserId(room.getUserId());
        roomInfo.setPassword(room.getPassword());
        roomInfo.setUpdateDt(room.getUpdateDt());
        if (room.getRoomType() == null) {
            roomInfo.setRoomType(1);
        } else {
            roomInfo.setRoomType(room.getRoomType());
        }
        roomInfo.setStopEndTime(room.getStopEndTime());

        if(room.getRoomType().equals(RoomTypeEnum.GAME_ROOM.getValue())){
            roomInfo.setGameId(room.getGameId());
            roomInfo.setGameStatus(room.getGameStatus());
            RestResult<List<GameResp>> gameList = gameService.getGameList();
            for (GameResp gameResp : gameList.getResult()) {
                if(gameResp.getGameId().equals(room.getGameId())){
                    roomInfo.setGameResp(gameResp);
                }
            }
        }

        if (room.getStopEndTime() != null) {
            Date currentDate = new Date();
            roomInfo.setCurrentTime(currentDate);
            Boolean isStop = currentDate.compareTo(room.getStopEndTime()) < 0;
            roomInfo.setStop(isStop);
            if (!isStop) {
                roomInfo.setStopEndTime(null);
                roomInfo.setCurrentTime(null);
            }
        } else {
            roomInfo.setStop(false);
        }

        TUser userInfo = userService.getUserInfo(room.getUserId());
        if (userInfo != null) {
            RespRoomUser roomUser = new RespRoomUser();
            roomUser.setPortrait(userInfo.getPortrait());
            roomUser.setUserId(userInfo.getUid());
            roomUser.setUserName(userInfo.getName());
            if(userInfo.getSex()==null){
                userInfo.setSex(1);
            }
            roomUser.setSex(UserSexEnum.parse(userInfo.getSex().intValue()).getDesc());
            roomInfo.setCreateUser(roomUser);
        } else {
            // 删除 房间
            try {
                this.chrmDelete(room.getUid());
            } catch (Exception e) {
                log.error("[room list][user is null][delete err:{}]", e.getMessage());
            }
        }

        //添加用户统计数量
        roomInfo.setUserTotal(this.getRoomUserTotal(room.getUid()));

        return roomInfo;
    }

    @Override
    public Integer getRoomUserTotal(String roomId) {
        Object total = roomHashOperations.get(REDIS_ROOM_USER_TOTAL_KEY, roomId);
        if (total == null) {
            return 0;
        }
        log.info("[room:" + roomId + "] is count:" + total);
        return (Integer) total;
    }

    @Override
    public void roomManageUserCheck(TRoom room, String userId) throws BusinessException {
        if(!room.getUserId().equals(userId)){
            Set<String> manageUserIds = setOperations.members(getRedisRoomManageUserKey(room.getUid()));
            if(manageUserIds != null && !manageUserIds.contains(userId)){
                throw new RestException(RestResultCode.ERR_NOT_AUTH.getCode(), RestResultCode.ERR_NOT_AUTH.getMsg());
            }
        }
    }

    @Override
    public RestResult<Boolean> userExist(String roomId, String userId) {
        if(StringUtils.isBlank(roomId)||StringUtils.isBlank(userId)){
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        IMUserExistResultInfo resultInfo = imHelper.userExist(roomId, userId);
        if(resultInfo==null||resultInfo.getCode().intValue()!=200){
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
        return RestResult.success(resultInfo.getIsInChrm());
    }


    private void updateRoomUserTotal(String roomId, Integer num) {
        log.info("[room:" + roomId + "] is user count:" + num);
        redisHashOperations.increment(REDIS_ROOM_USER_TOTAL_KEY, roomId, num);
    }


    private String getRedisRoomIdsKey() {
        return KeyGen.getRedisRoomIdsKey();
    }

    private String getRedisRoomIdsKey(Integer type) {
        return KeyGen.getRedisRoomIdsKey(type);
    }

    /**
     * @description: 房间人员统计
     * @date: 2021/6/4 9:35
     * @author: by zxw
     **/
    private String REDIS_ROOM_USER_TOTAL_KEY = RoomKeysCons.REDIS_ROOM_USER_TOTAL_KEY;

    private String getRedisRoomInfosKey() {
        return KeyGen.getRedisRoomInfosKey();
    }

    private String getRedisRoomGagUserKey(String roomId) {
        return  KeyGen.getRedisRoomGagUserKey(roomId);
    }

    private String getRedisRoomManageUserKey(String roomId) {
        return KeyGen.getRedisRoomManageUserKey(roomId);
    }

    private String getRedisRoomGiftKey(String roomId) {
        return KeyGen.getRedisRoomGiftKey(roomId);
    }


   /* private String getRedisRoomPkStopKey(String pkId) {
        return CustomerConstant.SERVICENAME + "|pk|stop|" + pkId;
    }*/

    @PostConstruct
    private void init() throws Exception {
//       initChrmWhiteList();
    }

    /**
     * 添加聊天室消息白名单
     */
    private void initChrmWhiteList() throws Exception {

        List<String> chrmWhiteList = new ArrayList<>(
                Arrays.asList("RCMic:transferHostMsg", "RCMic:takeOverHostMsg", "RC:chrmKVNotiMsg", "RCMic:chrmSysMsg", "RCMic:gift", "RCMic:broadcastGift", "RCMic:chrmMsg"));

        //查询 IM 添加的消息白名单

        IMChrmWhiteListResult result = imHelper.chrmWhitelistQuery();
        if (!result.isSuccess()) {
            return;
        }
        List<String> imChrmWhiteList = result.getWhitlistMsgType();
        if (imChrmWhiteList == null) {
            imChrmWhiteList = new ArrayList<>();
        }
        imChrmWhiteList.remove("");
        List<String> finalImChrmWhiteList = imChrmWhiteList;

        List<String> needAdds = chrmWhiteList.stream().filter(item -> !finalImChrmWhiteList.contains(item)).collect(toList());
        List<String> needDeletes = finalImChrmWhiteList.stream().filter(item -> !chrmWhiteList.contains(item)).collect(toList());

        if (!needDeletes.isEmpty()) {
            imHelper.chrmWhitelistDelete(needDeletes);
        }

        if (!needAdds.isEmpty()) {
            imHelper.chrmWhitelistAdd(needAdds);
        }
    }

    @Override
    public RestResult callImToKeepAlive(ChrTaskType chrTaskType) {
        try {
            List<TTaskMic> list = taskMicService.taskList(chrTaskType.getValue());
            if (CollectionUtils.isEmpty(list)) {
                return RestResult.success("list is null");
            }
            for (TTaskMic tTaskMic : list) {
                ChrmKeepAliveMessage message = new ChrmKeepAliveMessage();
                message.setContent(tTaskMic.getMsg());
                IMApiResultInfo imApiResultInfo = imHelper.publishMessage(tTaskMic.getUserId(), tTaskMic.getRoomId(), message);
                log.info("==keepAliveRoom:{}", JSON.toJSONString(imApiResultInfo));
            }
            return RestResult.success();
        } catch (Exception e) {
            return RestResult.generic(RestResultCode.ERR_OTHER, e.getMessage());
        }
    }

    @Override
    public RestResult keepAlive(String userId, String roomId, String msg) {
        try {
            taskMicService.addTask(ChrTaskType.keepRoomAlive.getValue(), userId + ":" + roomId, userId, roomId);
            return RestResult.success();
        } catch (Exception e) {
            return RestResult.generic(RestResultCode.ERR_OTHER, e.getMessage());
        }
    }

    @Override
    public RestResult cancelKeepAlive(String userId, String roomId) {
        try {
            taskMicService.delTask(ChrTaskType.keepRoomAlive.getValue(), userId, roomId);
            return RestResult.success();
        } catch (Exception e) {
            return RestResult.generic(RestResultCode.ERR_OTHER, e.getMessage());
        }
    }

}
