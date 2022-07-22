package cn.rongcloud.mic.room.pojos;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqRoomSetting {

    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("房间id")
    private String roomId;
//    private Boolean allowedJoinRoom;
//    private Boolean allowedFreeJoinMic;

    /**
     * @description: 申请上麦模式
     * @date: 2021/6/2 9:30
     * @author: by zxw
     **/
    @ApiModelProperty("申请上麦模式")
    private Boolean applyOnMic;

    /**
     * @description: 全麦锁麦
     * @date: 2021/6/2 9:31
     * @author: by zxw
     **/
    @ApiModelProperty("全麦锁麦")
    private Boolean applyAllLockMic;

    /**
     * @description: 全麦锁座
     * @date: 2021/6/2 9:32
     * @author: by zxw
     **/
    @ApiModelProperty("全麦锁座")
    private Boolean applyAllLockSeat;

    /**
     * @description: 静音
     * @date: 2021/6/2 9:33
     * @author: by zxw
     **/
    @ApiModelProperty("静音")
    private Boolean setMute;

    /**
     * @description: 设置座位数量，默认8位
     * @date: 2021/6/2 9:34
     * @author: by zxw
     **/
    @ApiModelProperty("设置座位数量，默认8位")
    private Integer setSeatNumber = 8;
}
