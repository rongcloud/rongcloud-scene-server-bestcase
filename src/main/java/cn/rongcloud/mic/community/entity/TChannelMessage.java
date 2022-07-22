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
 * 频道消息表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@Data
@TableName("t_channel_message")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TChannelMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 唯一标识
	 */
	private String uid;
	/**
	 * 频道uid
	 */
	private String channelUid;
	/**
	 * 消息uid
	 */
	private String messageUid;
	/**
	 * 消息类型,0标记的消息
	 */
	private Integer messageType;
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
