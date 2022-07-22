package cn.rongcloud.mic.game.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GameStatusReq {


    @ApiModelProperty(value = "房间id",required = true)
    private String roomId;

    @ApiModelProperty(value = "游戏id")
    private String gameId;

    @ApiModelProperty(value = "：1 未开始 2 游戏中")
    private Integer gameStatus;

}
