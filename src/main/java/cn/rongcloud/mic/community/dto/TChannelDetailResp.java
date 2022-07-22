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
public class TChannelDetailResp {

    @ApiModelProperty(value = "频道唯一标识",required = true)
    private String uid;

    @ApiModelProperty(value = "频道名称",required = true)
    private String name;

    @ApiModelProperty(value = "不用关心这个字段,后端排序用，前端按照集合顺序")
    private int sort;

    @ApiModelProperty(value = "通知类型")
    private Integer noticeType;

    private String groupUid;

}
