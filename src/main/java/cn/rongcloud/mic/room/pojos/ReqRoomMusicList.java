package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqRoomMusicList {
    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("房间id")
    private String roomId;

    @NotNull(message = "type should not be null")
    @ApiModelProperty("聊天室类型 0 官方，1 自定义")
    private Integer type;

}
