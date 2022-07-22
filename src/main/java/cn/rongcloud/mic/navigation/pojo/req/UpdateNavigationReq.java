package cn.rongcloud.mic.navigation.pojo.req;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 *@Author gaoxin
 *@Date 2021/9/7 4:10 下午
 *@Version 1.0
 **/
public class UpdateNavigationReq {

	@NotBlank(message = "id should not be blank")
	@ApiModelProperty("id")
	private Long id;

	@NotBlank(message = "name should not be blank")
	@ApiModelProperty("名称")
	private String name;

	@NotBlank(message = "desc should not be blank")
	@ApiModelProperty("描述")
	private String desc;

	@ApiModelProperty("背景图")
	private String coverImage;

	@ApiModelProperty("布局：0垂直，1平铺")
	private Integer layout;

	@ApiModelProperty("生效与否：0生效，1不生效")
	private Integer effectiveType;
}
