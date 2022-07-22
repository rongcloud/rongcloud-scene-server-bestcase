package cn.rongcloud.mic.community.enums;

public enum ChannelMessageTypeEnum {

    JOIN(1,"加入"),
    MARK(2, "标记消息"),
    SHUTUP(3,"被禁言"),
    NOSHUTUP(4,"解除禁言"),
    NO_MARK(5, "解除标记消息");

    private int value;

    private String desc;


    ChannelMessageTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static ChannelMessageTypeEnum parse(String value) {
        for (ChannelMessageTypeEnum cse : ChannelMessageTypeEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }


}
