package cn.rongcloud.mic.navigation.pojo.resp;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@Author gaoxin
 *@Date 2021/9/7 4:04 下午
 *@Version 1.0
 **/
@Data
public class NavigationInfoResp {

	@ApiModelProperty("id")
	private Long id;

	@ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("描述")
	private String desc;

	@ApiModelProperty("背景图")
	private String coverImage;

	@ApiModelProperty("布局：0垂直，1平铺")
	private Integer layout;

	@ApiModelProperty("生效与否：0生效，1不生效")
	private Integer effectiveType;

	@ApiModelProperty("创建人")
	private String createUser;

	@ApiModelProperty("创建时间")
	private Date createDt;

	@ApiModelProperty("修改人")
	private String updateUser;

	@ApiModelProperty("修改时间")
	private Date updateDt;
}
