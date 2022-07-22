package cn.rongcloud.mic.game.enums;

public enum GameStatusEnum {

    READY(1,"未开始"),
    PLAYING(2,"游戏中");

    private int value;

    private String desc;


    GameStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static GameStatusEnum parse(String value) {
        for (GameStatusEnum cse : GameStatusEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }

}
