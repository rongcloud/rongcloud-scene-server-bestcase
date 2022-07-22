package cn.rongcloud.mic.room.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.ReqCloudPlayerSync;
import cn.rongcloud.mic.room.pojos.ReqVideoRoomStatusSync;
import cn.rongcloud.mic.room.service.VideoRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mic/video/room")
@Slf4j
@Api(tags = "同步相关")
public class VideoRoomController {

    @Autowired
    VideoRoomService videoRoomService;

    @PostMapping(value = "/status/sync")
    @ApiOperation("语聊房房间状态同步")
    public RestResult statusSync(@RequestBody ReqVideoRoomStatusSync roomStatusSync) {
        log.info("video room status sync, data:{}", GsonUtil.toJson(roomStatusSync));
        return videoRoomService.statusSync(roomStatusSync);
    }

    @PostMapping(value = "/cloudplayer/sync")
    @ApiOperation("云播放回调")
    public RestResult cloudPlayerSync(@RequestBody ReqCloudPlayerSync cloudPlayerSync) {
        log.info("video room cloudplayer sync, data:{}", GsonUtil.toJson(cloudPlayerSync));
        return RestResult.success();
    }


}
