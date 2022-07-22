package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReqVideoRoomStatusSync {

    @ApiModelProperty("房间id")
    private String roomId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("事件类型")
    private Integer event;

    @ApiModelProperty("时间戳")
    private Long timestamp;

    @ApiModelProperty("退出类型")
    private Integer exitType;

}
