package cn.rongcloud.mic.community.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TCommunityUserInfoResp {

    @ApiModelProperty(value = "用户唯一标识",required = true)
    private String userUid;

    @ApiModelProperty(value = "社区昵称",required = true)
    private String nickName;

    @ApiModelProperty(value = "用户头像",required = true)
    private String portrait;

    @ApiModelProperty(value = "用户社区状态 1:审核中,2:审核未通过,3:已进入，4:退出,5:被踢出",required = true)
    private Integer status;
}
