package cn.rongcloud.mic.room.enums;

public enum RoomType {
    /**
     * 官方
     * 创建聊天室
     */
    OFFICIAL(0),
    /**
     * 自建
     * 加入聊天室
     */
    CUSTOM(1),

    /**
     * @description: 退出聊天室
     * @date: 2021/6/2 10:35
     * @author: by zxw
     **/
    EXIT(2),

    /**
     * @description: 销毁聊天室
     * @date: 2021/6/2 10:35
     * @author: by zxw
     **/
    DESTROY(3);

    private int value;

    RoomType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
