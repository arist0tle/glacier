package alpha.common.base.log;

import alpha.common.base.constant.Constants;
import alpha.common.base.constant.FieldType;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HaiyangServer on 2019/9/21.
 */
@Log4j2
public class TLogTest {
    private static TLog log = LogFactory.getLogger(TLogTest.class);

    public static void main(String[] args) {
        log.info(11111);

        List<String> list = new ArrayList<>();
        list.add("alpha");
        list.add("algorithm");
        list.add("ccc");
        log.info(list);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aaa","bbb");
        jsonObject.put("test", "value");
        jsonObject.put("name", "nagle");
        log.info(jsonObject);

        FieldType fieldType = FieldType.DATETIME;
        log.info(fieldType);

        String constants = Constants.N;
        log.info(constants);

        HashMap<String,Object> map = new HashMap<>();
        map.put("key", "value");
        log.info(map);

    }
}
