package cn.rongcloud.mic.room.pojos.callback;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 审核 回调Dto
 */
@Data
public class AuditCallBackDto implements Serializable {

    @ApiModelProperty("回调类型1：审核开始;2:审核结束;3:审核状态异常 ;4: 审核结果回调")
    private Integer type;

    @ApiModelProperty("审核任务的状态码")
    private Integer code;

    @ApiModelProperty("当前审核任务对应的 房间 ID")
    private String roomId;

    @ApiModelProperty("当前音视频流 对应的用户 ID")
    private String userId;

    @ApiModelProperty("当前音视频流 所属的会话 ID")
    private String sessionId;

    @ApiModelProperty("审核事件详情")
    private AuditCallBackContentDto content;

}
