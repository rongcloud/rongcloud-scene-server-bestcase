package cn.rongcloud.mic.user.enums;

public enum  UserStatusEnum {

    NOMAL(0,"正常"),
    FREEZE(1, "freeze");

    private int value;

    private String desc;


    UserStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static UserStatusEnum parse(String value) {
        for (UserStatusEnum cse : UserStatusEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }


}
