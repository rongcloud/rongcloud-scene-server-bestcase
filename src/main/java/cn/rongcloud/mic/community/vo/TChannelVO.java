package cn.rongcloud.mic.community.vo;


import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 频道表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@ApiModel(value = "TChannelVO", description = "频道表")
public class TChannelVO implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("自定义唯一标识")
	private String uid;


	@ApiModelProperty("频道名称")
	private String name;

	@ApiModelProperty("频道描述")
	private String remark;

}
