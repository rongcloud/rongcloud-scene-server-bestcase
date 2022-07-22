package cn.rongcloud.mic.room.pojos;

import java.util.List;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/8
 */
@Data
public class ReqRoomUserGag {

    @NotBlank(message = "roomId should not be blank")
    private String roomId;

    @NotBlank(message = "operation should not be blank")
    @ApiModelProperty("操作，add:禁言, remove:解除禁言")
    private String operation;

    @NotBlank(message = "userIds should not be blank")
    @ApiModelProperty("被禁言的用户id")
    private List<String> userIds;
}
