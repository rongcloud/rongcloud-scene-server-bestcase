package cn.rongcloud.mic.room.pojos;

import java.util.Date;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Data
public class ResRoomCreate {

    private String roomId;

    private String roomName;

    private String themePictureUrl;

    private String backgroundUrl;

    private Integer isPrivate;

    private String password;

    private String userId;

    private Date updateDt;
}
