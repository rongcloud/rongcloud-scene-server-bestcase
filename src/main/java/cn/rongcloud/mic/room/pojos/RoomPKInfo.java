package cn.rongcloud.mic.room.pojos;

import lombok.Data;

@Data
public class RoomPKInfo {

    /**
     * 对方房间ID
     */
    private String toRoomId;

    /**
     * 状态：0pk中；1暂停pk;2取消pk
     */
    private Integer status;
    /**
     * 开始pk时间戳
     */
    private Long time;

    /**
     * 代表当前PK 的唯一id
     */
    private String pkId;

    /**
     * 房主
     */
    private String userId;

    /**
     *
     */
    private boolean leader;
}
