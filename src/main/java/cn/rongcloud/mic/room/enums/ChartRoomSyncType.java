package cn.rongcloud.mic.room.enums;

public enum ChartRoomSyncType {
    /**
     * 创建聊天室
     */
    CREATE(0),
    /**
     * 加入聊天室
     */
    JOIN(1),
    /**
     * 退出聊天室
     */
    QUIT(2),
    /**
     * 销毁聊天室
     */
    DESTORY(3);

    private int value;

    ChartRoomSyncType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
