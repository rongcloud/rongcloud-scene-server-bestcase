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
 * 社区表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@TableName("t_community")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TCommunity implements Serializable {
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
	 * 社区名称
	 */
	private String name;
	/**
	 * 社区头像
	 */
	private String portrait;

	/**
	 * 社区封面
	 */
	private String coverUrl;

	/**
	 * 通知类型
	 */
	private Integer noticeType;

	/**
	 * 默认加入频道uid
	 */
	private String joinChannelUid;

	/**
	 * 默认通知频道uid
	 */
	private String msgChannelUid;

	/**
	 * 社区描述
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 是否需要审核,0:不需要,1:需要
	 */
	private Integer needAudit;
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
