package cn.rongcloud.mic.room.mapper;

import cn.rongcloud.mic.room.model.TRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoomMapper extends BaseMapper<TRoom> {

    List<TRoom> findRoomPage(@Param("roomType") Integer roomType, @Param("sex") Integer sex,
                             @Param("gameId") String gameId, @Param("start") Integer start,
                             @Param("end") Integer end);

    long findRoomPageTotal(@Param("roomType") Integer roomType, @Param("sex") Integer sex,
                           @Param("gameId") String gameId);

    TRoom selectOneByGameId(@Param("gameId") String gameId);
}
