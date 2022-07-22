package cn.rongcloud.mic.community.dto;

import cn.rongcloud.mic.common.page.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TCommunityUserReq  extends PageParam {

    @ApiModelProperty(value = "社区唯一id",required = true)
    private String communityUid;

    @ApiModelProperty(value = "查询类型,0:在线和离线全部,1:在线,2:离线,3:封禁,4:禁言",required = true)
    private Integer selectType;

    @ApiModelProperty("昵称")
    private String nickName;

}
