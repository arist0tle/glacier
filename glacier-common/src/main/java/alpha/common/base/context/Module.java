package alpha.common.base.context;

import alpha.common.base.util.MXBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author tanghaiyang on 2018/9/27.
 */
@Slf4j
public class Module {
    private final static LocalHost LOCALHOST = new LocalHost();
    public static final String KEY_SEPARATOR = "#";
    public static String moduleName;
    public static String host;
    public static Integer pid;

    static {
        try {
            String path = Module.class.getResource("/").getPath();
            if (path.contains("/target/")) {
                moduleName = StringUtils.substringBeforeLast(path, "/target/");
                moduleName = StringUtils.substringAfterLast(moduleName, File.separator);
            } else {
                moduleName = StringUtils.substringBeforeLast(path, "/lib/");
                moduleName = StringUtils.substringAfterLast(moduleName, File.separator);
            }
            host = LOCALHOST.getIp();
            pid = MXBeanUtils.getRuntimePID();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String getName() {
        return moduleName;
    }

    public static String getFullName() {
        return String.join(KEY_SEPARATOR, moduleName, host, String.valueOf(pid));
    }
}
