package cn.rongcloud.mic.room.pojos;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/8
 */
@Data
public class ReqRoomId {

    @NotBlank(message = "roomId should not be blank")
    private String roomId;
}
