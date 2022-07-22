package cn.rongcloud.mic.room.enums;

public enum ChrTaskType {
    /**
     * -
     */
    keepRoomAlive("keepRoomAlive"),
    ;

    private String value;

    ChrTaskType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
