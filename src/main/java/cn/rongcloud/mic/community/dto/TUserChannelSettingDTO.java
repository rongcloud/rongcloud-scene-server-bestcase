package cn.rongcloud.mic.community.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户频道设置表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TUserChannelSettingDTO implements Serializable {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty("频道uid")
	private String channelUid;


	@ApiModelProperty("0:所有消息都接收,1:被@时通知，2:从不通知")
	private Integer noticeType;


}
