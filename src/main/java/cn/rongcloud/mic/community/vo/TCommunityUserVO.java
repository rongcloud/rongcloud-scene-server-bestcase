package cn.rongcloud.mic.community.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户社区表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Data
@ApiModel(value = "TCommunityUserVO", description = "用户社区表")
public class TCommunityUserVO implements Serializable {
	private static final long serialVersionUID = 1L;



	@ApiModelProperty("自增id")
	private Integer id;


	@ApiModelProperty("社区uid")
	private String communityUid;


	@ApiModelProperty("用户uid")
	private String userUid;


	@ApiModelProperty("0:进入,1:审核，2:退出,3:被踢出")
	private Integer status;


	@ApiModelProperty("用户群昵称")
	private String userNick;


	@ApiModelProperty("是否被禁言0:没有,1:禁言")
	private Integer shutUp;


	@ApiModelProperty("是否被封禁0:没有,1:封禁")
	private Integer freezeFlag;


	@ApiModelProperty("0:所有消息都接收,1:被@时通知，2:从不通知")
	private Integer noticeType;


	@ApiModelProperty("是否删除")
	private Integer delFlag;


	@ApiModelProperty("创建时间")
	private Date createTime;


	@ApiModelProperty("更新时间")
	private Date updateTime;



}
