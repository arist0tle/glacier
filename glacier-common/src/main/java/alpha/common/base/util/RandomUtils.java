package alpha.common.base.util;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * @author tanghaiyang on 2019/2/22.
 */
@Slf4j
public class RandomUtils {

    private static final Random random = new Random();

    private RandomUtils() {
    }

    public static int randomInt(int max) {
        return random.nextInt(max);
    }

    public static String randomString(int length) {
        String raw = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456";
        int rawLength = raw.length();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < length; i++) {
            ret.append(raw.charAt(random.nextInt(rawLength)));
        }
        return ret.toString();
    }

    public static int randomInt(int[] arr) {
        int length = arr.length;
        int seq = random.nextInt(length);
        return arr[seq];
    }

    public static String randomString(String[] arr) {
        int length = arr.length;
        int seq = random.nextInt(length);
        return arr[seq];
    }

    public static <T> T random(List<T> arr) {
        int length = arr.size();
        int seq = random.nextInt(length);
        return arr.get(seq);
    }

}
