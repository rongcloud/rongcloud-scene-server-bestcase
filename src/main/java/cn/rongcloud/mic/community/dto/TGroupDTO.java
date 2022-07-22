package cn.rongcloud.mic.community.dto;


import cn.rongcloud.mic.common.page.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分组表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@Data
@ApiModel(value = "TGroupDTO", description = "分组表")
public class TGroupDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;



	@ApiModelProperty("自增id")
	private Integer id;


	@ApiModelProperty("自定义唯一标识")
	private String uid;


	@ApiModelProperty("分组名称")
	private String name;


	@ApiModelProperty("排序字段")
	private Integer sort;


	@ApiModelProperty("分组描述")
	private String remark;


	@ApiModelProperty("创建人")
	private String creator;


	@ApiModelProperty("是否删除")
	private Integer delFlag;


	@ApiModelProperty("创建时间")
	private Date createTime;


	@ApiModelProperty("更新时间")
	private Date updateTime;



}
