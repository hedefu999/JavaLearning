package misc.cachedemo;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 过期缓存检测线程
 */
public class ExpireThread implements Runnable {
    static {
        ExpireThread self = new ExpireThread();
        Executors.newSingleThreadExecutor().submit(self);
        //new Thread(self).start();
    }
    @Override
    public void run() {
        while (true){
            try {
                //每10秒检测缓存过期
                TimeUnit.SECONDS.sleep(2);
                expireCache();
            }catch (Exception e){e.printStackTrace();}
        }
    }
    private void expireCache(){
        for (String key : CacheGlobal.concurrentMap.keySet()){
            CacheValue cache = CacheGlobal.concurrentMap.get(key);
            //缓存存活时间
            long expireTime = cache.getExpireTime()*1000 + cache.getWriteTime();
            //缓存过期要清除，缓存清除策略有：定时、惰性、定期三种，这里定时
            if (System.currentTimeMillis() > expireTime){
                System.out.println(key + "被清理");
                CacheGlobal.concurrentMap.remove(key);
            }
        }
    }
}
