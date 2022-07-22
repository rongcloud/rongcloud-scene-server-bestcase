package cn.rongcloud.mic.community.enums;

public enum CommunityUserStatusEnum {

    AUDITING(1,"审核中"),
    REFUSE(2,"拒绝"),
    JOIN(3, "已加入"),
    EXIT(4,"退出"),
    KICKED_OUT(5,"被踢出");

    private int value;

    private String desc;


    CommunityUserStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static CommunityUserStatusEnum parse(String value) {
        for (CommunityUserStatusEnum cse : CommunityUserStatusEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }


}
