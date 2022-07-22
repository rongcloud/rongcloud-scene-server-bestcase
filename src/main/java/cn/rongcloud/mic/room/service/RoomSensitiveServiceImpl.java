package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.common.constant.CustomerConstant;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.room.dao.RoomDao;
import cn.rongcloud.mic.room.dao.RoomSensitiveDao;
import cn.rongcloud.mic.room.model.TRoomSensitive;
import cn.rongcloud.mic.room.pojos.ResSensitive;
import cn.rongcloud.mic.room.pojos.ResSensitiveList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoomSensitiveServiceImpl implements RoomSensitiveService {

    @Autowired
    private RoomSensitiveDao roomSensitiveDao;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomDao roomDao;

    @Override
    public RestResult sensitiveAdd(ResSensitive resSensitive, JwtUser jwtUser) {
        RestResult result = roomService.checkData(resSensitive.getRoomId(), jwtUser);

        if (!result.isSuccess()) {
            return result;
        }
        try {
            TRoomSensitive sensitive = new TRoomSensitive();
            sensitive.setRoomId(resSensitive.getRoomId());
            sensitive.setName(resSensitive.getName());
            sensitive.setCreateDt(new Date());
            TRoomSensitive roomSensitiveRes = roomSensitiveDao.save(sensitive);
            String REDIS_KEY = this.getSensitiveListRedisKey(resSensitive.getRoomId());
            redisUtils.del(REDIS_KEY);
            return RestResult.success(roomSensitiveRes);
        } catch (Exception e) {
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
    }

    @Override
    public RestResult sensitiveEdit(ResSensitive resSensitive, JwtUser jwtUser) {
        RestResult result = roomService.checkData(resSensitive.getRoomId(), jwtUser);

        if (!result.isSuccess()) {
            return result;
        }
        if (resSensitive.getId() == null || resSensitive.getId().compareTo(0L) <= 0) {
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }
        try {
            roomSensitiveDao.updateRoomBackground(resSensitive.getId(), resSensitive.getName());
            String REDIS_KEY = this.getSensitiveListRedisKey(resSensitive.getRoomId());
            redisUtils.del(REDIS_KEY);
            return RestResult.success();
        } catch (Exception e) {
            return RestResult.generic(RestResultCode.ERR_OTHER);
        }
    }

    @Override
    public RestResult sensitiveDelete(Long id, JwtUser jwtUser) {
        TRoomSensitive sensitive = this.getRoomSensitive(id);
        if (sensitive == null) {
            return RestResult.generic(RestResultCode.ERR_NOT_FIND);
        }
//        TRoom room = roomDao.findTRoomByUserIdEquals(jwtUser.getUserId());
//        if (room == null) {
//            return RestResult.generic(RestResultCode.ERR_ROOM_NOT_EXIST);
//        }
//        if (!sensitive.getRoomId().equals(room.getUid())) {
//            return RestResult.generic(RestResultCode.ERR_NOT_AUTH);
//        }
        String REDIS_KEY = this.getSensitiveListRedisKey(sensitive.getRoomId());
        redisUtils.del(REDIS_KEY);
        roomSensitiveDao.delete(sensitive);
        return RestResult.success();
    }

    @Override
    public RestResult sensitiveList(String roomId, JwtUser jwtUser) {
//        RestResult result = this.checkData(roomId, jwtUser);
//
//        if (!result.isSuccess()) {
//            return result;
//        }

        List<TRoomSensitive> list = this.getSensitiveByRoomId(roomId);
        if (list == null || list.size() == 0) {
            return RestResult.success();
        }
        List<ResSensitiveList> responses = new ArrayList<>();
        for (TRoomSensitive sensitive : list) {
            responses.add(this.buildSensitive(sensitive));
        }
        return RestResult.success(responses);
    }

    /**
     * 敏感词列表 缓存
     *
     * @param roomId
     * @return
     */
    private List<TRoomSensitive> getSensitiveByRoomId(String roomId) {
        String REDIS_KEY = this.getSensitiveListRedisKey(roomId);
        if (redisUtils.hasKey(REDIS_KEY)) {
            Object obj = redisUtils.get(REDIS_KEY);
            return (List<TRoomSensitive>) obj;
        }
        List<TRoomSensitive> list = roomSensitiveDao.findAllByRoomIdEquals(roomId);
        redisUtils.set(REDIS_KEY, list, CustomerConstant.CACHE_TIME);
        return list;
    }

    //敏感词redisKey
    private String getSensitiveListRedisKey(String roomId) {
        return CustomerConstant.SERVICENAME + ":room_sensitive_list:" + roomId;
    }


    private ResSensitiveList buildSensitive(TRoomSensitive sensitive) {
        if (sensitive == null) {
            return null;
        }
        ResSensitiveList response = new ResSensitiveList();
        response.setId(sensitive.getId());
        response.setName(sensitive.getName());
        response.setCreateDt(sensitive.getCreateDt());
        return response;
    }

    private TRoomSensitive getRoomSensitive(Long id) {
        //先从缓存查询，不存在则从数据库查询
        String key = CustomerConstant.SERVICENAME + ":room_sensitive_key:" + id;
        Object sensitive = redisUtils.get(key);
        if (sensitive != null) {
            return (TRoomSensitive) sensitive;
        }
        log.info("get sensitive info from db, roomId:{}", id);
        Optional<TRoomSensitive> optional = roomSensitiveDao.findById(id);
        if (optional.isPresent()) {
            redisUtils.set(key, optional.get(), 2 * 60 * 60);
            return optional.get();
        }

        return null;
    }


}
