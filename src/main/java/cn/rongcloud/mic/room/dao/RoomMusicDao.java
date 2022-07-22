package cn.rongcloud.mic.room.dao;

import cn.rongcloud.mic.room.model.TRoomMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoomMusicDao extends JpaRepository<TRoomMusic, Long> {
    List<TRoomMusic> findTRoomMusicByTypeEqualsOrderBySortAsc(Integer type);

    List<TRoomMusic> findTRoomMusicByTypeGreaterThanAndRoomIdEqualsOrderBySortAsc(Integer type, String roomId);

    TRoomMusic findTRoomMusicByTypeEqualsAndNameEquals(Integer type, String name);

    TRoomMusic findTRoomMusicByTypeEqualsAndNameEqualsAndRoomIdEquals(Integer type, String name, String roomId);

    @Modifying
    @Transactional
    @Query(value = "delete from TRoomMusic t where t.id=?1")
    void deleteTRoomMusicByIdEquals(Long id);

    /**
     * @description: （官方）显示房间当前当前排序的前一个数据
     * @date: 2021/6/3 9:26
     * @author: by zxw
     **/
    @Transactional
    @Query(nativeQuery = true, value = "select * from t_room_music t where t.room_id=?1 and t.type=0 and t.sort<?2 order by t.sort desc limit 1")
    TRoomMusic findOfficialPreBySort(String roomId, Integer sort);

    @Transactional
    @Query(nativeQuery = true, value = "select * from t_room_music t where t.room_id=?1 and t.type!=0 and t.sort<?2 order by t.sort desc limit 1")
    TRoomMusic findPreBySort(String roomId, Integer sort);

    /**
     * @description: 显示房间当前当前排序的下一个数据
     * @date: 2021/6/3 9:26
     * @author: by zxw
     **/
    @Transactional
    @Query(nativeQuery = true, value = "select * from t_room_music t where t.room_id=?1 and t.type=0 and t.sort>?2 order by t.sort asc limit 1")
    TRoomMusic findOfficialNextBySort(String roomId, Integer sort);

    @Transactional
    @Query(nativeQuery = true, value = "select * from t_room_music t where t.room_id=?1 and t.type!=0 and t.sort>?2 order by t.sort asc limit 1")
    TRoomMusic findNextBySort(String roomId, Integer sort);


    /**
     * @description: 显示房间下最大的sort
     * @date: 2021/6/3 9:24
     * @author: by zxw
     **/
    @Transactional
    @Query(nativeQuery = true, value = "select ifnull(max(t.sort),0) as sort from t_room_music t where t.room_id=?1")
    Integer getMaxSort(String roomId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update t_room_music t  set t.sort=?2 where t.id=?1")
    int updateRoomMusic(Long id, Integer sort);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update t_room_music t  set t.sort= t.sort + 1 where t.room_id=?1 and t.sort >?2")
    int updateRoomMusicBySort(String roomId, Integer sort);


    List<TRoomMusic> findTRoomMusicByThirdMusicIdEqualsAndRoomIdEquals(String thirdMusicId, String roomId);

    @Transactional
    @Query(nativeQuery = true, value = "select * from t_room_music t where t.id=?1")
    TRoomMusic findTroomMusicById(Long id);
}
