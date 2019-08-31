package com.redis._07sentinel;

import com.redis.CommonTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * redis配置了主从复制后客户端连接的是sentinel哨兵，此处连接哨兵进行测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:07sentinel/07sentinel.xml")
public class SentinelTest {
    private static String testKeyName = "newKeyOnSentinel";

    /**
     * 使用javaAPI直接写sentinel的客户端
     * @param ar
     */
    public static void main(String[] ar){
        String masterPass = "mainserver";
        Set<String> sentinels = new HashSet<>(Arrays.asList(
                "127.0.0.1:6360",
                "127.0.0.1:6361",
                "127.0.0.1:6362"
        ));
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(5);
        JedisSentinelPool sentinelPool =
                new JedisSentinelPool("mymaster",sentinels,jedisPoolConfig,masterPass);
        Jedis jedis = sentinelPool.getResource();
        jedis.set(testKeyName,"XHTSG");
        String value = jedis.get(testKeyName);
        System.out.println(value);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Test
    public void testSentinelConfig(){
        SessionCallback callback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                ops.boundValueOps(testKeyName).set("设置值");
                String value = (String) ops.boundValueOps(testKeyName).get();
                return value;
            }
        };
        String result = (String) redisTemplate.execute(callback);
    }
}
