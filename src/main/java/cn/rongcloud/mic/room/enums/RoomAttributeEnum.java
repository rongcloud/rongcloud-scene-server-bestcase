package cn.rongcloud.mic.room.enums;

/**
 * 1聊天室 2电台 3视频直播
 * @author zhouxingbin
 */

public enum RoomAttributeEnum {
    /**
     * 音频房间
     */
    Audio(1),
    /**
     * 电台
     */
    RadioStation(2),
    /**
     * 视频房间
     */
    Vedio(3),

    ;

    private int value;

    RoomAttributeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
