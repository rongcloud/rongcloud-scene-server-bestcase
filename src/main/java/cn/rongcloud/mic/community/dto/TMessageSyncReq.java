package cn.rongcloud.mic.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 全量消息表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-15 14:50:03
 */

@Data
public class TMessageSyncReq implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 发送用户uid
	 */
	private String fromUserId;
	/**
	 * 接收用户uid
	 */
	private String toUserId;
	/**
	 * 消息类型
	 */
	private String objectName;

	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 消息uid
	 */
	private String msgUID;
	/**
	 * 二人会话是 1 、讨论组会话是 2 、群组会话是 3 、聊天室会话是 4 、客服会话是 5 、 系统通知是 6 、应用公众服务是 7 、公众服务是 8
	 */
	private String channelType;
	/**
	 * 服务端收到客户端发送消息时的服务器时间
	 */
	private String msgTimestamp;
	/**
	 * 消息的发送源头
	 */
	private String source;
	/**
	 * 会话频道 Id
	 */
	private String busChannel;

}
