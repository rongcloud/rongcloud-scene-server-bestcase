package cn.rongcloud.mic.game.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GameResp implements Serializable {

    @ApiModelProperty("游戏id")
    private String gameId;
    /**
     * 游戏名称
     */
    @ApiModelProperty("游戏名称")
    private String gameName;
    /**
     * 游戏简介
     */
    @ApiModelProperty("游戏简介")
    private String gameDesc;
    /**
     * 游戏缩略图
     */
    @ApiModelProperty("游戏缩略图")
    private String thumbnail;
    /**
     * 游戏加载中的背景图
     */
    @ApiModelProperty("游戏加载中的背景图")
    private String loadingPic;
    /**
     * 游戏模式，代表几支队伍，目前都是1支队伍，默认 1
     */
    @ApiModelProperty("游戏模式")
    private int gameMode;
    /**
     * 游戏最少人数
     */
    @ApiModelProperty("游戏最少人数")
    private int minSeat;
    /**
     * 游戏最大人数
     */
    @ApiModelProperty("游戏最大人数")
    private int maxSeat;

}
