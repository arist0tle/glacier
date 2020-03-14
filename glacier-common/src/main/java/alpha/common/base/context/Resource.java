package alpha.common.base.context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.text.MessageFormat;

/**
 * @author tanghaiyang on 2017/11/07.
 */
public class Resource {

    public static String getActiveProfile() {
        Environment env = SpringContext.getEnv();
        String activeProfile = env.getActiveProfiles()[0];
        return MessageFormat.format("application-{}.properties", activeProfile);
    }

    public static String getPath(String resourceLocation) {
        try {
            return ResourceUtils.getURL("classpath:" + resourceLocation).getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getJarPath() {
        String path = getResourcePath();
        if (path.contains("/target/")) {
            path = StringUtils.substringBefore(path, "target/");
            return path + "target/";
        } else if (path.endsWith("/lib/")) {
            return StringUtils.substringBefore(path, "lib/");
        }
        return path;
    }

    public static String getPath() {
        String path = getResourcePath();
        if (path.contains("/target/")) {
            if (path.contains("/test-classes/")) {
                return path.replace("test-classes", "classes");
            }
        } else {
            return path.replace("/lib/", "/conf/");
        }
        return path;
    }

    private static String getResourcePath() {
        String path = Resource.class.getResource("/").getPath();
        return path;
    }

    public static String getResourcePath(String resourceLocation) {
        String path = Resource.class.getResource(resourceLocation).getPath();
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        //System.out.println(Resource.getPath("mysql.sql"));
        System.out.println(Resource.getPath());
        System.out.println(Resource.getJarPath());
        System.out.println(Resource.getResourcePath());
    }
}
