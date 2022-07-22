package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.ReqVideoRoomStatusSync;

public interface VideoRoomService {
    RestResult statusSync(ReqVideoRoomStatusSync roomStatusSync);

}
