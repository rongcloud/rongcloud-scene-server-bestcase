package cn.rongcloud.mic.room.enums;

public enum RoomUserGagOperation {
    /**
     * 添加
     */
    ADD("add"),
    /**
     * 移除
     */
    REMOVE("remove");

    private String value;

    RoomUserGagOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
