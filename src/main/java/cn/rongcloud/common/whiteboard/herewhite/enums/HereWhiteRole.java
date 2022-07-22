package cn.rongcloud.common.whiteboard.herewhite.enums;

/**
 * HereWhite 权限角色，可取 admin、writer、reader（必填）
 */
public enum HereWhiteRole {
    /**
     * admin
     */
    ADMIN("admin"),
    /**
     * writer
     */
    WRITER("writer"),

    /**
     * reader
     */
    READER("reader");


    private String value;

    HereWhiteRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
