package alpha.common.base.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author tanghaiyang on 2018/4/9.
 */
public class MapUtils {

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> mapStream = map.entrySet().stream();
        // asc
        mapStream.sorted(Map.Entry.comparingByKey())
                .forEach(x -> result.put(x.getKey(), x.getValue()));
        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> mapStream = map.entrySet().stream();
        // desc
        Comparator<Map.Entry<K, V>> comparator = Map.Entry.comparingByValue(Comparator.reverseOrder());
        mapStream.sorted(comparator).forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
}
