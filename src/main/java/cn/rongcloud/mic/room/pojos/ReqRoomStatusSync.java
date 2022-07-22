package cn.rongcloud.mic.room.pojos;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/8
 */
@Data
public class ReqRoomStatusSync {

    @ApiModelProperty("聊天室 Id")
    private String chatRoomId;

    @ApiModelProperty("用户 Id 数据")
    private List<String> userIds;

    @ApiModelProperty("操作状态：0 直接调用接口、1 触发融云退出聊天室机制将用户踢出、2 用户被封禁、3 触发融云销毁聊天室机制自动销毁")
    private Integer status;

    @ApiModelProperty("聊天室事件类型：0 创建聊天室、1 加入聊天室、2 退出聊天室、3 销毁聊天室")
    private Integer type;

    @ApiModelProperty("发生时间")
    private Long time;
}
