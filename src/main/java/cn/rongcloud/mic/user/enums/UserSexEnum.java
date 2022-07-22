package cn.rongcloud.mic.user.enums;

public enum UserSexEnum {

    UNKNOWN(0,"未知"),
    MAN(1, "男"),
    WOMAN(2,"女");

    private int value;

    private String desc;


    UserSexEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static UserSexEnum parse(int value) {
        for (UserSexEnum cse : UserSexEnum.values()) {
            if (value==cse.getValue()) {
                return cse;
            }
        }
        return null;
    }


}
