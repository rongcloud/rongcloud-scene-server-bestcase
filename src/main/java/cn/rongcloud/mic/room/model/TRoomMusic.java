package cn.rongcloud.mic.room.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_room_music")
@Data
public class TRoomMusic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("音乐id")
    private Long id;

    @ApiModelProperty("音乐名称")
    private String name;

    @ApiModelProperty("音乐作者")
    private String author;


    @Column(name = "room_id")
    @ApiModelProperty("聊天室id")
    private String roomId;

    @ApiModelProperty("type 0 官方  1 本地添加 2 从官方添加")
    private Integer type;

    private String url;
    private String size;
    @Column(name = "create_dt")
    private Date createDt;
    @Column(name = "update_dt")
    private Date updateDt;
    private Integer sort;

    @ApiModelProperty("音乐背景图片")
    private String backgroundUrl;

    @ApiModelProperty("第三方音乐id")
    private String thirdMusicId;

}
