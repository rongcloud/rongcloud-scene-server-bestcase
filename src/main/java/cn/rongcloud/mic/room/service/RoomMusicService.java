package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.model.TRoomMusic;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicAdd;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicDelete;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicList;
import cn.rongcloud.mic.room.pojos.ReqRoomMusicMove;

import java.util.List;

public interface RoomMusicService {

    RestResult<List<TRoomMusic>> musicList(ReqRoomMusicList data);

    RestResult addMusic(ReqRoomMusicAdd data, JwtUser jwtUser);

    RestResult musicDelete(ReqRoomMusicDelete data);

    RestResult musicMove(ReqRoomMusicMove data, JwtUser jwtUser);

    RestResult musicPlay(ReqRoomMusicDelete data);

    RestResult<TRoomMusic> musicPlayGet(String roomId);
}
