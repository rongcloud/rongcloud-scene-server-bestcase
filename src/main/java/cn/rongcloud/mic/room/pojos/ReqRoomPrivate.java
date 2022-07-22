package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqRoomPrivate {

    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("房间id")
    private String roomId;

    @NotNull(message = "isPrivate should not be null")
    @ApiModelProperty("是否上锁")
    private Integer isPrivate;

    @ApiModelProperty("房间密码")
    private String password;
}
