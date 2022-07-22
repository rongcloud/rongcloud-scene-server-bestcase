package cn.rongcloud.common.im.pojos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RTCRoomMemberResp implements Serializable {

    private int code;

    private String roomId;

    private String sessionId;

    private long createTime;

    private List<Members> members;

    @Data
    public static class Members{

        private String userId;

        private long joinTime;

    }

}
