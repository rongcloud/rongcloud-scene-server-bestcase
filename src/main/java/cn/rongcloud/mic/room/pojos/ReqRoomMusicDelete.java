package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ReqRoomMusicDelete {
    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("聊天室 Id")
    private String roomId;

    @NotNull(message = "id should not be null")
    @ApiModelProperty("音乐id")
    private Long id;

}
