package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ReqRoomGiftAdd {
    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("房间id")
    private String roomId;

    @NotNull(message = "giftId should not be null")
    @ApiModelProperty("数量")
    private Integer num;

    @NotNull(message = "giftId should not be null")
    @ApiModelProperty("礼物id")
    private Integer giftId;

    @NotNull(message = "toUid should not be null")
    @ApiModelProperty("接收人Id")
    private String toUid;

}
