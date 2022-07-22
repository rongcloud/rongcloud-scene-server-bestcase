package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.ReqRoomPk;
import cn.rongcloud.mic.room.pojos.ResRoomPkInfo;
import cn.rongcloud.mic.room.pojos.ResRoomPkScore;

public interface RoomPkService {

    RestResult pk(ReqRoomPk data, JwtUser jwtUser);

    /**
     * 获取pk单个房间信息(老接口)
     * @param roomId
     * @return
     */
    RestResult<ResRoomPkInfo> pkInfo(String roomId);

    RestResult<Boolean> isPK(String roomId);

    /**
     * 获取房间PK 信息，主要包含两个房间的积分信息，当前时间距pk 的时间差
     * @param roomId
     * @return
     */
    RestResult<ResRoomPkScore> getPkDetail(String roomId);

    /**
     * pk 阶段送礼物
     * @param roomId
     * @param formUid
     * @param toUid
     * @param currentValue
     */
    void pkGift(String roomId, String formUid, String toUid, Long currentValue);

    /**
     * 清 pk 信息
     * @param roomId
     */
    void cleanCacheByStopPk(String roomId);

}
