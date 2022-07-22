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
 * 频道表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@TableName("t_channel")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TChannel implements Serializable {
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
	 * 社区uid
	 */
	private String communityUid;
	/**
	 * 分组uid
	 */
	private String groupUid;
	/**
	 * 频道名称
	 */
	private String name;
	/**
	 * 排序字段
	 */
	private Integer sort;

	/**
	 * 频道描述
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
