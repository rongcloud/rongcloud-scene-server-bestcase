package cn.rongcloud.mic.community.vo;


import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 频道消息表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@Data
@ApiModel(value = "TChannelMessageVO", description = "频道消息表")
public class TChannelMessageVO implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("唯一uid")
	private String uid;

	@ApiModelProperty("频道uid")
	private String channelUid;


	@ApiModelProperty("消息uid")
	private String messageUid;


	@ApiModelProperty("创建人uid")
	private String creator;

}
