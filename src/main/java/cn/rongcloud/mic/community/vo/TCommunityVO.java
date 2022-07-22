package cn.rongcloud.mic.community.vo;


import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 社区表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@ApiModel(value = "TCommunityVO", description = "社区表")
public class TCommunityVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("社区唯一标识")
	private String communityUid;

	@ApiModelProperty("社区名称")
	private String name;

	@ApiModelProperty("社区头像")
	private String portrait;

	@ApiModelProperty("社区封面")
	private String coverUrl;

	@ApiModelProperty("已加入人数")
	private Integer personCount;

	@ApiModelProperty("社区描述")
	private String remark;
}
