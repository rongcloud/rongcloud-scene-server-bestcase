package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TCommunityDetailResp {

    @ApiModelProperty(value = "社区号，搜索用")
    private Integer id;

    @ApiModelProperty(value = "社区唯一标识",required = true)
    private String uid;

    @ApiModelProperty(value = "社区名称")
    private String name;

    @ApiModelProperty("社区头像")
    private String portrait;

    @ApiModelProperty("社区封面")
    private String coverUrl;

    @ApiModelProperty("社区描述")
    private String remark;

    @ApiModelProperty("社区创建人id")
    private String creator;

    @ApiModelProperty("是否需要审核，0:不需要，1：需要")
    private Integer needAudit;

    @ApiModelProperty(value = "创建者设置的社区通知类型")
    private Integer noticeType;

    @ApiModelProperty("用户默认进入的频道")
    private String joinChannelUid;

    @ApiModelProperty("默认消息频道")
    private String msgChannelUid;

    @ApiModelProperty(value = "修改类型,1:修改分组，2:修改频道,3:修改社区",required = true)
    private Integer updateType;

    @ApiModelProperty("已加入人数")
    private Integer personCount;

    @ApiModelProperty("社区和用户属性相关的")
    private CommunityUser communityUser;

    @ApiModelProperty("分组列表")
    private List<TGroupDetailResp> groupList;

    @ApiModelProperty("频道列表")
    private List<TChannelDetailResp> channelList;

    @Data
    public static class CommunityUser{

        @ApiModelProperty(value = "社区审核状态,0:展示加入，1:审核中，2:审核未通过，3:不展示文字描述(用户已经加入过了)")
        private Integer auditStatus;

        @ApiModelProperty(value = "用户设置的社区通知类型")
        private Integer noticeType;

        @ApiModelProperty("是否被禁言0:没有,1:禁言")
        private Integer shutUp;

        @ApiModelProperty("社区昵称")
        private String nickName;

    }

}
