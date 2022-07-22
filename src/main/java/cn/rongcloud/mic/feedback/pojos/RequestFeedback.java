package cn.rongcloud.mic.feedback.pojos;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RequestFeedback {

    @ApiModelProperty("是否好评")
    private Boolean isGoodFeedback;

    @ApiModelProperty("反馈说明")
    private String reason;
}
