package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TCommunityUpdateReq {

    @ApiModelProperty(value = "社区唯一id",required = true)
    private String uid;

    @ApiModelProperty(value = "社区名称")
    private String name;

    @ApiModelProperty(value = "社区头像")
    private String portrait;

    @ApiModelProperty("社区背景图片")
    private String coverUrl;

    @ApiModelProperty("社区描述")
    private String remark;

    @ApiModelProperty("社区是否需要审核，0:不需要,1:需要")
    private Integer needAudit;

    @ApiModelProperty(value = "创建者设置的社区通知类型")
    private Integer noticeType;

    @ApiModelProperty("用户默认进入的频道")
    private String joinChannelUid;

    @ApiModelProperty("默认消息频道")
    private String msgChannelUid;

}
