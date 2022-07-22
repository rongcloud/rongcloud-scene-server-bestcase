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
 * 用户社区表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@TableName("t_community_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TCommunityUser implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 自增id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 社区uid
	 */
	private String communityUid;
	/**
	 * 用户uid
	 */
	private String userUid;
	/**
	 * 0:进入,1:审核，2:退出,3:被踢出
	 */
	private Integer status;
	/**
	 * 用户群昵称
	 */
	private String nickName;
	/**
	 * 是否被禁言0:没有,1:禁言
	 */
	private Integer shutUp;
	/**
	 * 是否被封禁0:没有,1:封禁
	 */
	private Integer freezeFlag;
	/**
	 * 0:所有消息都接收,1:被@时通知，2:从不通知
	 */
	private Integer noticeType;
	/**
	 * 用户关于频道的设置，json结构
	 */
	private String channelSetting;

	/**
	 * 用户在线状态
	 */
	private Integer onlineStatus;

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
