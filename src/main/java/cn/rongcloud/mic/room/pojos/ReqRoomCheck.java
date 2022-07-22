package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangyaoyu
 * @create 2022/5/31 18:26
 */
@Data
public class ReqRoomCheck {

    @ApiModelProperty("房间类型：1聊天室； 2电台； 3视频直播； 4游戏房。")
    private Integer roomType=1;
}
