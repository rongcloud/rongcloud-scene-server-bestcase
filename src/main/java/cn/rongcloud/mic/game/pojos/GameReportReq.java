package cn.rongcloud.mic.game.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GameReportReq {

    @ApiModelProperty(value = "游戏id",required = true)
    private String gameId;

    @ApiModelProperty(value = "用户id",required = true)
    private String userId;

    @ApiModelProperty(value = "appId",required = true)
    private String appId;

    @ApiModelProperty(value = "环境：0 测试 1 线上",required = true)
    private Integer envFlag;

}
