package alpha.common.base.constant.login;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author tanghaiyang on 2018/12/26.
 */
public final class LoginContext {

    private static final ThreadLocal LOCAL = new ThreadLocal();
    private static final String LOGIN_USER_KEY = "login_user";

    public static String getUserIdStr(){
        return String.valueOf(getUserId());
    }

    public static Long getUserId(){
        LoginUser user = getUser();
        return Objects.nonNull(user) ? user.getUserId() : 0;
    }

    public static LoginUser getUser(){
        return get(LOGIN_USER_KEY, LoginUser.class);
    }

    public static void setUser(LoginUser loginUser){
        set(LOGIN_USER_KEY, loginUser);
    }

    private static <T> T get(String key, Class<T> tClass) {
        Map<String, Object> map = (Map)LOCAL.get();
        if (map == null){
            return null;
        }
        return (T) map.get(key);
    }

    private static void set(String key, Object value){
        Map<String, Object> map = (Map)LOCAL.get();
        if (map == null){
            map = new HashMap();
            LOCAL.set(map);
        }
        map.put(key, value);
    }
}
