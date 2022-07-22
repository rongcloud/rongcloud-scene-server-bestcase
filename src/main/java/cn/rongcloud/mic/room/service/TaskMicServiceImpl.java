package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.room.dao.TaskMicDao;
import cn.rongcloud.mic.room.model.TTaskMic;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/25
 */
@Service
public class TaskMicServiceImpl implements TaskMicService {

    @Autowired
    private TaskMicDao taskMicDao;

    @Override
    public TTaskMic getTask(String taskType, String taskKey) {
        TTaskMic tTaskMic = new TTaskMic();
        tTaskMic.setTaskType(taskType);
        tTaskMic.setTaskKey(taskKey);
        Example example = Example.of(tTaskMic);
        Optional<TTaskMic> optional = taskMicDao.findOne(example);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public void addTask(String taskType, String taskKey, String userId, String roomId) {
        TTaskMic tTaskMic = new TTaskMic();
        tTaskMic.setTaskType(taskType);
        tTaskMic.setTaskKey(taskKey);
        tTaskMic.setUserId(userId);
        tTaskMic.setRoomId(roomId);
        tTaskMic.setCreateTime(LocalDateTime.now());
        tTaskMic.setUpdateTime(LocalDateTime.now());
        tTaskMic.setDelFlag(0);
        taskMicDao.save(tTaskMic);
    }

    @Override
    public List<TTaskMic> taskList(String taskType) {
        TTaskMic tTaskMic = new TTaskMic();
        tTaskMic.setDelFlag(0);
        tTaskMic.setTaskType(taskType);
        return taskMicDao.findAll(Example.of(tTaskMic));
    }

    @Override
    public void delTask(String taskType, String userId, String roomId) {
//        TTaskMic tTaskMic = new TTaskMic();
//        tTaskMic.setUserId(userId);
//        tTaskMic.setRoomId(roomId);
//        tTaskMic.setTaskType(taskType);
//        taskMicDao.delete(tTaskMic);
        taskMicDao.delTask(taskType, userId, roomId);
    }

}
