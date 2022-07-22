package cn.rongcloud.mic.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分组表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@Data
@TableName("t_group")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 自定义唯一标识
	 */
	private String uid;
	/**
	 * 社区id
	 */
	private String communityUid;
	/**
	 * 分组名称
	 */
	private String name;
	/**
	 * 排序字段
	 */
	private Integer sort;
	/**
	 * 分组描述
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 是否删除
	 */
	private Integer delFlag;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;



}
