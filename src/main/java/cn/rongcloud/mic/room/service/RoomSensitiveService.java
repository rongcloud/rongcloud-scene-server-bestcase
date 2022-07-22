package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.ResSensitive;

public interface RoomSensitiveService {

    RestResult sensitiveAdd(ResSensitive resSensitive, JwtUser jwtUser);

    RestResult sensitiveEdit(ResSensitive resSensitive, JwtUser jwtUser);

    RestResult sensitiveDelete(Long id, JwtUser jwtUser);

    RestResult sensitiveList(String roomId, JwtUser jwtUser);

}
