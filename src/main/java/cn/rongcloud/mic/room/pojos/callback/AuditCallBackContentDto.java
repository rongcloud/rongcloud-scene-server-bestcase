package cn.rongcloud.mic.room.pojos.callback;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuditCallBackContentDto implements Serializable {

    // type = 1,2,3 （ 即 审核开始、审核结束 、审核状态异常 ）时

    @ApiModelProperty("审核流ID")
    private String streamId;

    @ApiModelProperty("发生事件事件戳")
    private Long timestamp;

    @ApiModelProperty("您开通审核时所选的 媒体类型 0：音频 ；1：视频    ")
    private Integer mediaType;

    @ApiModelProperty("审核任务出错时的错误信息")
    private String errorMessage;


    //当type=4 （ 审核结果回调 或 命中审核策略后的反馈）
    @ApiModelProperty("审核任务 ID")
    private String requestId;

    @ApiModelProperty("审核任务媒体类型：1 图片 2 声音")
    private Integer contentType;

    @ApiModelProperty("命中问题 的严重程度  1:PASS， 2: REVIEW，3: REJECT")
    private Integer riskLevel;

    @ApiModelProperty("审核命中的违规策略类型")
    private Integer riskType;

    @ApiModelProperty("视频流截帧图片违规发生的时间(绝对时间)  或 音频违规内容开始时间(绝对时间)")
    private Long riskTime;

    @ApiModelProperty("截图或声音片段地址 http/https")
    private String contentUrl;

    @ApiModelProperty("问题详细描述")
    private String desc;

    @ApiModelProperty("声音文本")
    private String matchText;

    @ApiModelProperty("声音匹配的敏感词")
    private String matchedItem;

    @ApiModelProperty("声音匹配的敏感词列表")
    private String matchedList;

}
