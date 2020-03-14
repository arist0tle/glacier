package alpha.common.base.constant;

/**
 * @author tanghaiyang on 2018/8/17.
 */
public enum FieldType {
    STRING("字符串"),
    LONG("整数"),
    DOUBLE("浮点数"),
    DATETIME("日期");

    private String label;

    FieldType(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }

    public boolean isNumber() {
        return equals(DOUBLE) || equals(LONG);
    }

    public boolean isText() {
        return equals(STRING) || equals(DATETIME);
    }
}
