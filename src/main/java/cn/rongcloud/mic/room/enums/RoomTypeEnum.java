package cn.rongcloud.mic.room.enums;

public enum RoomTypeEnum {

    CHAT_ROOM(1,"聊天室"),
    ELECT_ROOM(2, "电台"),
    VIDEO_ROOM(3,"直播"),
    GAME_ROOM(4, "游戏房"),
    KTV_ROOM(5, "ktv房");

    private int value;

    private String desc;


    RoomTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static RoomTypeEnum parse(String value) {
        for (RoomTypeEnum cse : RoomTypeEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }


}
