package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ResRoomScore {

    @ApiModelProperty("房间id")
    private String roomId;

    @ApiModelProperty("房间积分")
    private Long score;

    @ApiModelProperty("房主id")
    private String userId;

    @ApiModelProperty
    private boolean leader;

    @ApiModelProperty(value = "赠送积分用户列表",name = "userInfoList")
    private List<RespRoomUser> userInfoList;
}
