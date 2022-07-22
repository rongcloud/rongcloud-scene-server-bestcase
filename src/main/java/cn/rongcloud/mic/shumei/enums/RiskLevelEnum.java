package cn.rongcloud.mic.shumei.enums;

public enum  RiskLevelEnum {

    PASS(1,"PASS"),
    REVIEW(2, "REVIEW"),
    REJECT(3,"REJECT");

    private int value;

    private String desc;


    RiskLevelEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }


    public static RiskLevelEnum parse(String value) {
        for (RiskLevelEnum cse : RiskLevelEnum.values()) {
            if (value.equals(cse.getValue())) {
                return cse;
            }
        }
        return null;
    }


}
