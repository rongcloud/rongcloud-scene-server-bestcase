package cn.rongcloud.mic.room.pojos;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReqRoomType {

    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("房间id")
    private String roomId;

    @NotBlank(message = "roomType should not be blank")
    @ApiModelProperty("类型：1聊天室； 2电台")
    private Integer roomType;
}
