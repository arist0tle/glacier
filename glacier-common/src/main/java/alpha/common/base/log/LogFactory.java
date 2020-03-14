package alpha.common.base.log;

/**
 * Created by HaiyangServer on 2019/9/21.
 */
public class LogFactory {
    public static TLog getLogger(Class clazz){
        return new TLog(clazz);
    }
}
