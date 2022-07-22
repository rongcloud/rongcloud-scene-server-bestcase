package cn.rongcloud.mic.room.dao;

import cn.rongcloud.mic.room.model.TRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by sunyinglong on 2020/05/25.
 */
@Repository
public interface RoomDao extends JpaRepository<TRoom, Long> {

    TRoom findTRoomByUidEquals(String roomUid);

    @Transactional
    @Modifying
    @Query(value = "update TRoom t  set t.allowedJoinRoom=?2, t.allowedFreeJoinMic=?3 where t.uid=?1")
    int updateRoomSetting(String roomId, boolean allowedJoinRoom, boolean allowedFreeJoinMic);

    @Modifying
    @Transactional
    @Query(value = "delete from TRoom t where t.uid=?1")
    void deleteTRoomByUidEquals(String uid);

    @Transactional
    @Modifying
    @Query(value = "update TRoom t  set t.isPrivate=?2, t.password=?3 where t.uid=?1")
    int updateRoomPrivate(String roomId, Integer isPrivate, String password);

    @Transactional
    @Modifying
    @Query(value = "update TRoom t  set t.name=?2 where t.uid=?1")
    int updateRoomName(String roomId, String name);

    @Transactional
    @Modifying
    @Query(value = "update TRoom t  set t.backgroundUrl=?2 where t.uid=?1")
    int updateRoomBackground(String roomId, String backgroundUrl);

    @Transactional
    @Modifying
    @Query(value = "update TRoom t  set t.roomType=?2 where t.uid=?1")
    int updateRoomType(String roomId, Integer roomType);

    @Transactional
    @Modifying
    @Query(value = "update TRoom t  set t.stopEndTime=?2 where t.uid=?1")
    int updateStopEndTime(String roomId, Date stopEndTime);

    List<TRoom> findTRoomByUserIdEquals(String userId);

    TRoom findTRoomByUserIdAndRoomTypeEquals(String userId, Integer roomType);

    @Query(nativeQuery = true, value = "select t.uid from t_room_mic t left join t_user t1 on t.user_id = t1.uid where t1.belong_room_id = t.uid and t.room_type=?1 and t.uid not in (?2) limit 20")
    List<String> findOnlineList(Integer type, String roomId);

    List<TRoom> findTRoomByGameIdAndIsPrivateAndGameStatusEquals(String gameId, int isPrivate, int gameStatus, Pageable pageable);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update TRoom t  set t.gameId= ?2, t.gameStatus = ?3 where t.uid = ?1")
    void updateGameIdAndGameStatus(String roomId, String gameId, int i);
}