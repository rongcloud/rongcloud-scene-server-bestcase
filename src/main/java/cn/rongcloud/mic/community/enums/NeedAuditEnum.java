package cn.rongcloud.mic.community.enums;

public enum NeedAuditEnum {

    NO(0,"不需要"),
    YES(1, "需要");

    private int value;

    private String desc;


    NeedAuditEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static NeedAuditEnum parse(String value) {
        for (NeedAuditEnum cse : NeedAuditEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }


}
