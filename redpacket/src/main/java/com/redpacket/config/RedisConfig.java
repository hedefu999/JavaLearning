package com.redpacket.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
//@EnableCaching 与redisCacheManager一同配置，红包项目不需要
public class RedisConfig {
    private final Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Bean("redisStandaloneConfig")
    public RedisStandaloneConfiguration redisStandaloneConfig(){
        RedisStandaloneConfiguration redisConn = new RedisStandaloneConfiguration("localhost",6379);
        //redisConn.setPassword(null);
        return redisConn;
    }
    @Bean("redisPoolConfig")
    public JedisPoolConfig redisPoolConfig(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(8);
        poolConfig.setMaxTotal(15);
        poolConfig.setMaxWaitMillis(3000);
        return poolConfig;
    }
    /**
     * spring-data-redis 2.0 之后的JedisConnectionFactory的host设置方法被废弃，这里使用了新的设置方式
     * refto https://blog.csdn.net/dawn_after_dark/article/details/82112399
     * 使用RedisStandaloneConfiguration设置基本连接信息，但poolConfig的设置很麻烦
     * 似乎xml方式无法使用poolConfig
     * @param redisPoolConfig
     * @return
     */
    @Bean("redisConnFactory")
    public RedisConnectionFactory redisConnFactory(
            @Autowired RedisStandaloneConfiguration redisStandaloneConfig,
            @Autowired JedisPoolConfig redisPoolConfig){
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder builder =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        builder.poolConfig(redisPoolConfig);
        JedisClientConfiguration clientConfig = builder.build();
        JedisConnectionFactory connFactory = new JedisConnectionFactory(redisStandaloneConfig,clientConfig);
        //P622调用后的初始化方法，没有它将抛出异常。这里将生成的RedisConnectionFactory返回到了springIoC容器中，spring会自动调用afterpropertiesSet？
        connFactory.afterPropertiesSet();
        return connFactory;
    }
    @Bean
    public RedisTemplate redisTemplate(@Autowired RedisConnectionFactory redisConnFactory){
        // 自定Redis序列化器
        RedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();
        RedisSerializer stringSerializer = new StringRedisSerializer();
        RedisSerializer fastJsonSerializer = new GenericFastJsonRedisSerializer();
        // 定义RedisTemplate，并设置连接工程
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnFactory);
        // 设置序列化器
        redisTemplate.setDefaultSerializer(stringSerializer);
        //redisTemplate.setKeySerializer(stringSerializer);
        //redisTemplate.setValueSerializer(fastJsonSerializer);
        //redisTemplate.setHashKeySerializer(stringSerializer);
        //redisTemplate.setHashValueSerializer(fastJsonSerializer);
        return redisTemplate;
    }
    /**
     * spring-data-redis 2.0 下的CacheManager的创建方式
     */
    //@Bean("redisCacheManager")
    //public CacheManager redisCacheManager(@Autowired RedisConnectionFactory redisConnFactory){
    //    RedisCacheConfiguration redisCacheConfig =
    //            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10));
    //    RedisCacheManager redisCacheManager = RedisCacheManager
    //            .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnFactory))
    //            .cacheDefaults(redisCacheConfig).build();
    //    return redisCacheManager;
    //}
}
