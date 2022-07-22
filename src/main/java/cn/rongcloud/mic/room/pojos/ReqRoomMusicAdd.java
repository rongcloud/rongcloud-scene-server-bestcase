package cn.rongcloud.mic.room.pojos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqRoomMusicAdd {

    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("聊天室id")
    private String roomId;

    @NotNull(message = "name should not be null")
    @ApiModelProperty("音乐名称")
    private String name;

    @ApiModelProperty("音乐作者")
    private String author;

    @NotNull(message = "type should not be null")
    @ApiModelProperty("0 官方  1 本地添加 2 从官方添加,3 Hfive 添加")
    private Integer type;

    @NotNull(message = "url should not be null")
    @ApiModelProperty("音乐地址")
    private String url;
    //默认kb
    @ApiModelProperty("音乐大小")
    private Integer size;

    @ApiModelProperty("音乐背景图片")
    private String backgroundUrl;

    @ApiModelProperty("第三方音乐id")
    private String thirdMusicId;

}
