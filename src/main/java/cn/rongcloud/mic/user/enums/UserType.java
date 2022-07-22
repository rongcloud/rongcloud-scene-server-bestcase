package cn.rongcloud.mic.user.enums;

/**
 * Created by sunyinglong on 2020/6/3
 */
public enum UserType {
    /**
     * 游客
     */
    VISITOR(0),
    /**
     * 用户
     */
    USER(1);

    private int value;

    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
