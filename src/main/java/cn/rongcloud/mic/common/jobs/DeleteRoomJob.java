package cn.rongcloud.mic.common.jobs;


import cn.rongcloud.mic.room.model.TRoom;
import cn.rongcloud.mic.room.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteRoomJob {

    private Logger logger = LoggerFactory.getLogger(DeleteRoomJob.class);

    @Autowired
    private RoomService roomService;

    // 第一次执行前延时10秒启动,每5分钟执行一次
    //TODO: 如果分布式部署，需要特殊处理定时任务实例，否则多台服务器同事处理定时任务
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 5 * 60 * 1000)
    private void sayHello() {
        logger.info("[room check job start.]");

        List<TRoom> list = roomService.getRoomListAll();
        logger.info("[room list]" + list);
        if (list != null && list.size() > 0) {
            for (TRoom room : list) {
                try {
                    roomService.removeRoom(room);
                } catch (Exception e) {
                    logger.error("[room check job][err:{}]", e.getMessage());
                }
            }
        }
        logger.info("[room check job end.]");
    }
}
