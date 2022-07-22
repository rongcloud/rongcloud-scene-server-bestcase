package cn.rongcloud.mic.community.dto;


import cn.rongcloud.mic.common.page.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 社区表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@ApiModel(value = "TCommunityDTO", description = "社区表")
public class TCommunityDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "社区号",required = false)
	private Integer id;


	@ApiModelProperty(value = "社区名称",required = false)
	private String name;

}
