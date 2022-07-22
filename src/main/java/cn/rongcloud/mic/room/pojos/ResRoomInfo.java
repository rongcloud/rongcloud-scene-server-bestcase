package cn.rongcloud.mic.room.pojos;

import cn.rongcloud.mic.game.pojos.GameResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Data
@ApiModel(value = "房间信息", description = "房间信息")
public class ResRoomInfo {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("房间id")
    private String roomId;

    @ApiModelProperty("房间名称")
    private String roomName;

    @ApiModelProperty("主题图片")
    private String themePictureUrl;

    @ApiModelProperty("背景图片")
    private String backgroundUrl;

    @ApiModelProperty("是否私密")
    private Integer isPrivate;

    @ApiModelProperty("房间密码")
    private String password;

    @ApiModelProperty("房间创建人")
    private String userId;

    @ApiModelProperty("房间创建时间")
    private Date updateDt;

    private RespRoomUser createUser;

    @ApiModelProperty("房间类型：1聊天室、2电台、3视频直播")
    private Integer roomType;

    @ApiModelProperty("房间统计用户数量")
    private Integer userTotal;

    @ApiModelProperty("暂停结束时间")
    private Date stopEndTime;

    @ApiModelProperty("当前日期")
    private Date currentTime;

    @ApiModelProperty("是否暂停")
    private boolean stop;

    @ApiModelProperty("游戏id")
    private String gameId;

    @ApiModelProperty("游戏状态")
    private Integer gameStatus;

    @ApiModelProperty("游戏详情")
    private GameResp gameResp;
}
