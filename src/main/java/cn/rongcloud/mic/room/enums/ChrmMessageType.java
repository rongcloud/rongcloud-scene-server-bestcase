package cn.rongcloud.mic.room.enums;

public enum ChrmMessageType {
    /**
     * 添加用户禁言
     */
    USER_GAG_ADD(0),
    /**
     * 移除用户禁言
     */
    USER_GAG_REMOVE(1);

    private int value;

    ChrmMessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
