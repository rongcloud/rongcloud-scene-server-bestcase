package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Data
public class ReqRoomCreate {

    @NotBlank(message = "room name should not be blank")
    @ApiModelProperty(value = "房间名",required = true)
    private String name;

    @ApiModelProperty(value = "主题图片")
    private String themePictureUrl;

    @ApiModelProperty(value = "是否私密 0:公开,1:私密",required = true)
    private Integer isPrivate;

    @ApiModelProperty(value = "私密密码")
    private String password;

    @ApiModelProperty(value = "背景图",required = true)
    private String backgroundUrl;

    @ApiModelProperty(value = "kv")
    private List<Map<String, String>> kv;
    //房间类型：1聊天室 2电台
    @ApiModelProperty(value = "房间类型 1聊天室、2电台、3视频直播、4游戏房")
    private Integer roomType = 1;

    private double version=0;

    @ApiModelProperty(value = "游戏房对应的gameId")
    private String gameId;

}
