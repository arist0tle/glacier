package alpha.common.base.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author tanghaiyang on 2017/11/13.
 */
@Component
public class SpringContext implements ApplicationContextAware {
    public static ApplicationContext context;
    public static Environment env;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContext.context = context;
        SpringContext.env = context.getEnvironment();
    }

    public static <T> T getBean(String beanId, Class<T> clazz) {
        return context.getBean(beanId, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Environment getEnv() {
        return env;
    }

    public static String getProperty(String key) {
        return getProperty(key, "");
    }

    public static String getProperty(String key, String defaultValue) {
        return env.getProperty(key, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        return env.getProperty(key, targetType);
    }
}