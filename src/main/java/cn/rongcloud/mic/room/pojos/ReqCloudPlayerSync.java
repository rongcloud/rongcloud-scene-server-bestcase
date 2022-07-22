package cn.rongcloud.mic.room.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReqCloudPlayerSync implements Serializable {

    private Long timestamp;

    private Integer type;

    private String app_key;

    private String room_id;

    private String rtmp_url;

    private String user_id;

    private String kind;

    private Integer code;

}
