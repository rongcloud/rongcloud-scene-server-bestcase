package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReqRoomMicManage {

    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("房间id")
    private String roomId;

    @NotBlank(message = "userId should not be blank")
    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "isManage should not be blank")
    @ApiModelProperty("是否管理员")
    private Boolean isManage;
}
