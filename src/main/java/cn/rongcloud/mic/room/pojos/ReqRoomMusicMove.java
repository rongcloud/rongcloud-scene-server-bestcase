package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Date: 2021/6/3 10:43
 * @Author: by zxw
 */
@Data
public class ReqRoomMusicMove {

    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("聊天室id")
    private String roomId;

    @NotNull(message = "fromId should not be null")
    @ApiModelProperty("音乐id 放到 toId 的后面")
    private Long fromId;

    @NotNull(message = "toId should not be null")
    @ApiModelProperty("音乐id")
    private Long toId;
}
