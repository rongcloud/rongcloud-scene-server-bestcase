package cn.rongcloud.mic.shumei.enums;

public enum AuditTypeEnum {

    AUDIT_START(1,"审核开始"),
    AUDIT_STOP(2, "审核结束"),
    AUDIT_ERROR(3,"审核状态异常"),
    AUDIT_CALLBACK(4,"审核结果回调");

    private int value;

    private String desc;


    AuditTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static AuditTypeEnum parse(String value) {
        for (AuditTypeEnum cse : AuditTypeEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }


}
