package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ResRoomPkInfo {

    @ApiModelProperty("房主id")
    private String userId;

    @ApiModelProperty("房间积分")
    private Long score;

    @ApiModelProperty("pk开始的时间戳")
    private Long pkTime;

    private boolean leader;

    private List<RespRoomUser> userInfoList;


}
