package misc.homemadecache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheGlobal {
    // 全局缓存对象
    public static ConcurrentMap<String, CacheValue> concurrentMap = new ConcurrentHashMap<>();
}
