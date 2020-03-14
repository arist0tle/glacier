package alpha.common.base.util;

import alpha.common.base.constant.FieldType;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author tanghaiyang on 2018/3/16.
 */
public class DataUtils {

    public static Object toObject(String value, String fieldType) {
        FieldType type;
        try {
            type = FieldType.valueOf(fieldType.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = FieldType.STRING;
        }
        return toObject(value, type);
    }

    public static Object toObject(String value, FieldType type) {
        switch (type) {
            case STRING:
                return value == null ? "" : value;
            case DOUBLE:
                return NumberUtils.toDouble(value, 0);
            case LONG:
                return NumberUtils.toLong(value, 0);
            case DATETIME:
                return value == null ? "" : value;
        }
        return value;
    }
}
