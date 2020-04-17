package misc.cachedemo;

import lombok.Data;

@Data
public class CacheValue<T> implements Comparable<CacheValue> {
    // 缓存键
    private String key;
    // 缓存值
    private T value;
    // 最后访问时间
    private long lastTime;
    // 创建时间
    private long writeTime;
    // 存活时间
    private long expireTime;
    // 命中次数
    private Integer hitCount;

    @Override
    public int compareTo(CacheValue o) {
        return hitCount.compareTo(o.hitCount);
    }
}