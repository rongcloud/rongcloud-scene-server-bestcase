package cn.rongcloud.mic.room.enums;

public enum ChrmRoomPkStatus {

    /**
     * 开始PK
     */
    START(0),
    /**
     * 惩罚阶段
     */
    PUNISH(1),

    STOP(2)

    ;

    private int value;

    ChrmRoomPkStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
