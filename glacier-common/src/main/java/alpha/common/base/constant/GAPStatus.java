package alpha.common.base.constant;



/**
 * @author tanghaiyang on 2018/5/2.
 */
public enum GAPStatus implements Status {
    /**
     * alpha.common           -1~9999
     * crm-sys          10000~19999
     * crm-search       20000~29999
     * crm-tag          30000~39999
     * crm-biz          40000~49999
     * crm-persist      50000~59999
     * crm-recommend    60000~69999
     * crm-extension    1000000~
     */
    UNKNOWN(-1, "未知状态"),
    SUCCESS(1, "success"),
    ERROR(2, "error"),;
    private int code;
    private String desc;

    GAPStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
