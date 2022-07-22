package cn.rongcloud.mic.community.dto;


import cn.rongcloud.mic.common.page.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 频道消息表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@Data
@ApiModel(value = "TChannelMessageDTO", description = "频道消息表")
public class TChannelMessageDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "频道uid",required = true)
	private String channelUid;


}
