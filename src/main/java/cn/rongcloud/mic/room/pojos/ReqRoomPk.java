package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReqRoomPk {
    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty(value = "房间Id",required = true)
    private String roomId;

    @NotNull(message = "toRoomId should not be null")
    @ApiModelProperty(value = "对方房间Id",required = true)
    private String toRoomId;

    @NotNull(message = "status should not be null")
    @ApiModelProperty(value = "状态 0:开始，2:停止",required = true)
    private Integer status;

}
