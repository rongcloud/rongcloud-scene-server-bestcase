package cn.rongcloud.mic.room.dao;

import cn.rongcloud.mic.room.model.TTaskMic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/25
 */
@Repository
public interface TaskMicDao extends JpaRepository<TTaskMic, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from TTaskMic t where t.taskType=?1 and t.userId=?2 and t.roomId=?3")
    void delTask(String taskType, String userId, String roomId);
}
