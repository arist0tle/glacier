package alpha.common.base.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * @author tanghaiyang on 2017/12/27.
 */
public class CacheFactory {

    public static final int TIME_OUT_MINUTES = 10;

    public static <K, V> LoadingCache<K, V> create(CacheLoader loader) {
        return create(loader, TIME_OUT_MINUTES);
    }

    public static <K, V> LoadingCache<K, V> create(CacheLoader loader, long durationMins) {
        return CacheBuilder.newBuilder().refreshAfterWrite(durationMins, TimeUnit.MINUTES).build(loader);
    }
}
