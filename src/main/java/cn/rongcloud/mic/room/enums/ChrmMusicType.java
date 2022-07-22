package cn.rongcloud.mic.room.enums;

public enum ChrmMusicType {
    /**
     * 官方音乐
     */
    OFFICIAL(0),
    /**
     * 用户上传
     */
    CUSTOM(1),

    /**
     * 用户从官方列表添加
     */
    CUSTOM_OFFICIAL(2),

    /**
     * 第三方音乐
     */
    THIRD_MUSIC(3);


    private int value;

    ChrmMusicType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
