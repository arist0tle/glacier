package alpha.common.base.util;

/**
 * @author tanghaiyang on 2018/3/15.
 */
public class ClassUtils {

    public static Class<?> loadClass(String className) {
        if (className == null) {
            return null;
        }
        Class<?> klass = null;
        try {
            klass = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            //log.debug("Exception while loading class: " + className, ex);
        }
        return klass;
    }

    public static String jarForClass(String className) {
        Class<?> klass = loadClass(className);
        return jarForClass(klass);
    }

    public static String jarForClass(Class<?> klass) {
        return klass.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    private ClassUtils() {
        // Disable explicit object creation
    }
}
