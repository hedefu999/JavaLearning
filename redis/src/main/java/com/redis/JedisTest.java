package com.redis;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class JedisTest {
    private final Logger log = LoggerFactory.getLogger(JedisTest.class);

    private JedisPool jedisPool = null;
    @Before
    public void init(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(20);
        config.setMaxWaitMillis(2000);
        jedisPool = new JedisPool(config,"localhost");
    }

    @Test
    public void mains() {
        Jedis jedis = new Jedis("localhost",6379);
        int i = 0;
        try{
            long start = System.currentTimeMillis();
            while (true){
                if (System.currentTimeMillis() - start >= 1000){
                    break;
                }
                i++;
                jedis.set("test"+i,i+"");
            }
        }finally {
            jedis.close();
        }
        System.out.println("redis每秒操作："+i+"次");//17700
    }
    @Test
    public void testJedisPool(){

        Jedis jedis = jedisPool.getResource();
        Jedis jedis1 = jedisPool.getResource();
        JedisPubSub jedisPubSub = new InnerJedisPubSub();
        new Runnable(){
            @Override
            public void run() {
                log.info("在独立的线程中订阅频道");
                jedis.subscribe(jedisPubSub,"hedefu");
            }
        };

        jedis1.publish("hedefu","helloworld");
        System.out.println(jedisPubSub.isSubscribed());
        jedis1.publish("hedefu","helloworld2");


    }
}
class InnerJedisPubSub extends JedisPubSub {
    private final Logger log = LoggerFactory.getLogger(InnerJedisPubSub.class);

    @Override
    public void onMessage(String channel, String message) {
        log.info("{}频道收到消息：{}", channel,message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info("订阅了{}频道", channel);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.info("取消订阅{}频道。", channel);
    }

    @Override
    public boolean isSubscribed() {
        return super.isSubscribed();
    }
}