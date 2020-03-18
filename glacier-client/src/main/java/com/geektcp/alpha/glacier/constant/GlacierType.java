package com.geektcp.alpha.glacier.constant;

/**
 * @author haiyang on 3/14/20 12:54 PM.
 */
public enum  GlacierType {

    UNKNOWN(-1, "未知状态"),
    SUCCESS(1, "success"),
    ERROR(2, "error"),

    ;

    private int code;
    private String desc;

    GlacierType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
