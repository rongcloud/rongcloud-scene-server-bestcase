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
public class TGroupDetailResp {

    @ApiModelProperty(value = "分组唯一标识",required = true)
    private String uid;

    @ApiModelProperty(value = "分组名称",required = true)
    private String name;

    @ApiModelProperty(value = "不用关心这个字段,后端排序用，前端按照集合顺序")
    private int sort;

    @ApiModelProperty("频道列表")
    private List<TChannelDetailResp> channelList;

}
