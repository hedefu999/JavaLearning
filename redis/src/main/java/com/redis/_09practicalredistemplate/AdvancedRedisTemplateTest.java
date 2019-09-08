package com.redis._09practicalredistemplate;

import com.redis.CommonTestConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
//componentScan非要放在CommonTestConfig里才能生效，这里只好再配一个spring配置文件
//@ComponentScan(basePackageClasses = {RedisTemplateServiceImpl.class})
//@ComponentScan(basePackages = {"com.redis._09practicalredistemplate"})
@ContextConfiguration(locations = "classpath:09advancedredistemplate.xml")
public class AdvancedRedisTemplateTest extends CommonTestConfig {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * ValueOperations valueOperations = redisTemplate.opsForValue();
     * 并不能避免redisTemplate每次操作时重新获取连接
     */
    @Test
    public void testRedisTemplate(){
        redisTemplate.opsForValue().set("sky","蓝天");
        redisTemplate.opsForValue().set("teddy","bigger house");
        redisTemplate.opsForValue().get("name");
        /**上述操作会产生下述日志
         * Opening RedisConnection
         * Closing Redis Connection
         * Opening RedisConnection
         * Closing Redis Connection
         * Opening RedisConnection
         * Closing Redis Connection
         */
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("listen","whats that");
        valueOperations.set("hello","world");
        /**上述操作会产生五次获取redis连接的日志
         * Opening RedisConnection
         * Closing Redis Connection
         * Opening RedisConnection
         * Closing Redis Connection
         * Opening RedisConnection
         * Closing Redis Connection
         * Opening RedisConnection
         * Closing Redis Connection
         * Opening RedisConnection
         * Closing Redis Connection
         */
    }

    /**
     * 使用SessionCallback可以实现所有redis操作在一个redis连接下完成
     * 并且SessionCallback可以提供更多高级API简化开发
     * TODO RedisMessageListenerContainer是什么？
     */
    @Autowired
    private AdvancedRedisTemplateService iRedisTemplate;
    @Test
    public void testAdvancedRedisTemplate(){
        //iRedisTemplate.execMultiCommand();
        //iRedisTemplate.execTransaction();
        iRedisTemplate.execPipeline();
    }
}
