package alpha.common.base.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author tanghaiyang on 2018/9/27.
 */
public class MXBeanUtils {
    public static RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

    /**
     * Process ID
     *
     * @return
     */
    public static int getRuntimePID() {
        return NumberUtils.toInt(StringUtils.substringBefore(runtime.getName(), "@"), 0);
    }

    /**
     * Start time.
     *
     * @return
     */
    public static long getRuntimeStartTime() {
        return runtime.getStartTime();
    }
}

