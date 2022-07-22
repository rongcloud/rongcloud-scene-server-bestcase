package cn.rongcloud.mic.community.dto;


import cn.rongcloud.mic.common.page.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 全量消息表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@ApiModel(value = "TMessageDTO", description = "全量消息表")
public class TMessageDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;



	@ApiModelProperty("自增id")
	private Integer id;


	@ApiModelProperty("发送用户uid")
	private String fromUserId;


	@ApiModelProperty("接收用户uid")
	private String toUserId;


	@ApiModelProperty("消息类型")
	private String objectName;


	@ApiModelProperty("消息uid")
	private String messageUid;


	@ApiModelProperty("二人会话是 1 、讨论组会话是 2 、群组会话是 3 、聊天室会话是 4 、客服会话是 5 、 系统通知是 6 、应用公众服务是 7 、公众服务是 8")
	private Integer channelType;


	@ApiModelProperty("服务端收到客户端发送消息时的服务器时间")
	private String msgTimestamp;


	@ApiModelProperty("消息的发送源头")
	private String source;


	@ApiModelProperty("会话频道 Id")
	private String busChannel;


	@ApiModelProperty("消息类型,0标记的消息")
	private Integer messageType;


	@ApiModelProperty("创建人")
	private String creator;


	@ApiModelProperty("是否删除")
	private Integer delFlag;


	@ApiModelProperty("创建时间")
	private Date createTime;


	@ApiModelProperty("更新时间")
	private Date updateTime;



}
