package cn.rongcloud.mic.user.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqUpdate {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("头像")
    private String portrait;

    @ApiModelProperty("性别,1:男，2女")
    private Integer sex;

}
