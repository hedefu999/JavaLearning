package misc.cachedemo;


/**
 * 缓存操作工具类,添加和查询缓存
 */
public class CacheUtils {
    public static void put(String key, Object value, long expire){
        if (key == null || key.trim().length() == 0){
            return;
        }
        //缓存存在时就更新
        long current = System.currentTimeMillis();
        if (CacheGlobal.concurrentMap.containsKey(key)){
            CacheValue cache = CacheGlobal.concurrentMap.get(key);
            cache.setHitCount(cache.getHitCount()+1);
            cache.setWriteTime(current);
            cache.setLastTime(current);
            cache.setExpireTime(expire);
            cache.setValue(value);
        }else {
            CacheValue cache = new CacheValue();
            cache.setKey(key);
            cache.setValue(value);
            cache.setWriteTime(current);
            cache.setLastTime(current);
            cache.setHitCount(0);
            cache.setExpireTime(expire);
            CacheGlobal.concurrentMap.put(key,cache);
        }
    }
    //获取缓存
    public static Object get(String key){
        if (key == null || key.trim().length() == 0){
            return null;
        }
        if (!CacheGlobal.concurrentMap.containsKey(key)) return null;
        CacheValue cache = CacheGlobal.concurrentMap.get(key);
        if (cache == null) return null;
        //惰性删除
        if (System.currentTimeMillis() > cache.getExpireTime()*1000 + cache.getWriteTime()){
            CacheGlobal.concurrentMap.remove(key);
            return null;
        }
        cache.setHitCount(cache.getHitCount()+1);
        cache.setLastTime(System.currentTimeMillis());
        return cache.getValue();
    }

}
