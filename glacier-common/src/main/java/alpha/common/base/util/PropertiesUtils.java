package alpha.common.base.util;


import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author tanghaiyang on 2018/3/12.
 */
@Slf4j
public class PropertiesUtils {

    public static synchronized Properties load(String location) {
        InputStream is = null;
        Properties props = new Properties();
        try {
            is = PropertiesUtils.class.getResourceAsStream(location);
            if (is == null) {
                return props;
            }
            props.load(is);
        } catch (IOException e) {
            log.error("Reading properties file error!" + e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("Closing InputStream occurs error due to:" + e.toString());
                }
            }
        }
        return props;
    }

    public static synchronized Properties loadFile(String filePath) {
        Properties props = new OrderedProperties();
        InputStream in = null;
        try {
            in = new FileInputStream(filePath);
            props.load(in);
        } catch (Exception ex) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return props;
    }

    private static class OrderedProperties extends Properties {
        private static final long serialVersionUID = 1L;
        private final LinkedHashSet<Object> keys = new LinkedHashSet<>();

        public Enumeration<Object> keys() {
            return Collections.enumeration(keys);
        }

        public Object put(Object key, Object value) {
            keys.add(key);
            return super.put(key, value);
        }

        public Set<Object> keySet() {
            return keys;
        }

        public Set<String> stringPropertyNames() {
            Set<String> set = new LinkedHashSet<>();
            for (Object key : this.keys) {
                set.add((String) key);
            }
            return set;
        }
    }
}
