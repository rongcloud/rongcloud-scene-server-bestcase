package cn.rongcloud.common.im.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class RTCCloudPlayerReq implements Serializable {

    private String user_id;

    private String user_name;

    private Integer media_type;

    private Integer room_type;

    private String rtmp_url;

    private String callback_url;

    private String roomId;


}
