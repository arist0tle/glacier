package alpha.common.base.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by HaiyangServer on 2019/9/21.
 */
public class TLog {
    private static Logger log;

    public TLog(Class clazz) {
        log = LoggerFactory.getLogger(clazz);
    }

    public void info(Object obj) {
        log.info(obj.toString());
    }

    public void info(String s) {
        log.info(s);
    }

    public void info(String s, Throwable e){
        log.info(s, e);
    }

    public void debug(String s){
        log.debug(s);
    }

    public void debug(Object obj){
        log.debug(obj.toString());
    }

    public void error(String s){
        log.error(s);
    }

    public void error(Object obj){
        log.error(obj.toString());
    }

    public void error(String s, Throwable e){
        log.error(s, e);
    }

    public void warn(String s){
        log.warn(s);
    }

    public void warn(Object obj){
        log.warn(obj.toString());
    }

    public void warn(String s, Throwable e){
        log.warn(s, e);
    }

}
