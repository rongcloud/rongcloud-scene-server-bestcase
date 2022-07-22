package cn.rongcloud.mic.room.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sunyinglong on 2020/5/25
 */
@Entity
@Table(name = "t_room_mic")
@Data
public class TRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uid;
    private String name;
    @Column(name = "theme_picture_url")
    private String themePictureUrl;
    @Column(name = "background_url")
    private String backgroundUrl;
    @Column(name = "allowed_join_room")
    private boolean allowedJoinRoom;
    @Column(name = "allowed_free_join_mic")
    private boolean allowedFreeJoinMic;
    private Integer type;
    @Column(name = "is_private")
    private Integer isPrivate;
    private String password;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "create_dt")
    private Date createDt;
    @Column(name = "update_dt")
    private Date updateDt;
    @Column(name = "room_type")
    private Integer roomType = 1;  //默认语聊房
    @Column(name = "stop_end_time")
    private Date stopEndTime;
    @Column(name = "game_id")
    private String gameId;
    @Column(name = "game_status")
    private Integer gameStatus;
    @Deprecated
    @Transient
    private String gameName;
    //以下是新加房间设置字段，数据库不存储
    @Transient
    private Boolean applyOnMic;
    @Transient
    private Boolean applyAllLockMic;
    @Transient
    private Boolean applyAllLockSeat;
    @Transient
    private Boolean setMute;
    @Transient
    private Integer setSeatNumber;

}
