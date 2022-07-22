package cn.rongcloud.mic.room.task;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.enums.ChrTaskType;
import cn.rongcloud.mic.room.service.RoomService;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/25
 */
@Slf4j
@Data
@Component
public class KeepAliveTask implements Runnable {

    @Autowired
    private RoomService roomService;

    @Override
    public void run() {
        log.info("==:KeepAliveTask:begin:{}", ChrTaskType.keepRoomAlive);
        RestResult result = roomService.callImToKeepAlive(ChrTaskType.keepRoomAlive);
        log.info("==:KeepAliveTask:end:{}", JSON.toJSONString(result));
    }

}
