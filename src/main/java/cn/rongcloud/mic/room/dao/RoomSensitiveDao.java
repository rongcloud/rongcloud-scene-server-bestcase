package cn.rongcloud.mic.room.dao;

import cn.rongcloud.mic.room.model.TRoomSensitive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface RoomSensitiveDao extends JpaRepository<TRoomSensitive, Long> {

    @Transactional
    @Modifying
    @Query(value = "update TRoomSensitive t  set t.name=?2 where t.id=?1")
    int updateRoomBackground(Long id, String name);


    List<TRoomSensitive> findAllByRoomIdEquals(String roomId);
}
