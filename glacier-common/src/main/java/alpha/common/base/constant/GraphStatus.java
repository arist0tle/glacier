package alpha.common.base.constant;

public enum GraphStatus implements Status {

    UNKNOWN(-1, "未知状态"),
    SUCCESS(1, "success"),
    ERROR(2, "error"),
    MISS_PARAM(3, "缺少必要参数"),
    NOT_SUPPORTED(4, "暂不支持"),

    COPY_FAILE(101, "复制新对象失败"),

    DETAIL_FAILE(201, "查询详情失败"),
    DElETE_FAILE(302, "删除失败"),
    SAVE_OR_UPDATE_FAIL(303, "保存失败"),
    BATCH_SAVE_OR_UPDATE_FAIL(304, "批量保存失败"),
    FIND_FAIL(305, "查询失败"),
    FIND_PAGE_FAIL(306, "分页查询失败"),
    SAVE_PARAM_NULL(307, "保存对象不能为空"),
    DAO_NULL(308, "Dao对象未指定"),
    BUILD_FILTER_FAIL(309, "组装过滤条件失败"),

    URL_INVALID_ERROR(310, "无效的URL"),

    CALL_REST_ERROR(311, "调用Rest接口异常,{}"),

    REPEAT_ERROR(312, "操作失败，有重复的数据"),;


    private int code;
    private String desc;

    GraphStatus(int code, String desc) {
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
