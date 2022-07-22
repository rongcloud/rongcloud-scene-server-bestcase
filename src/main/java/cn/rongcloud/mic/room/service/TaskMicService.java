package cn.rongcloud.mic.room.service;

import cn.rongcloud.mic.room.model.TTaskMic;
import java.util.List;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/25
 */
public interface TaskMicService {

    TTaskMic getTask(String taskType, String taskKey);

    void addTask(String taskType, String taskKey, String userId, String roomId);

    List<TTaskMic> taskList(String taskType);

    void delTask(String taskType, String userId, String roomId);
}
