package com.redis._08springredis;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 报错解决记录：
 * 1.Failed to serialize object using DefaultSerializer; DefaultSerializer requires a Serializable payload but received an object of type
 * --- User implements Serializable
 * 2.Cannot enhance @Configuration bean definition since its singleton instance has been created too early. The typical cause is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor return type: Consider declaring such methods as 'static'.
 * ---
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringMybatisConfig.class,SpringRedisConfig.class})
public class TestSpringRedisCache {
    private final Logger log = LoggerFactory.getLogger(TestSpringRedisCache.class);
    @Autowired
    private UserBasicService userBasicService;

    @Test
    public void testC(){
        User user = new User("1886374","lucy",13);
        User user1 = userBasicService.saveUser(user);
        //主键无法获取，这样会造成使用主键做redis key出错，所以新建user的可以考虑不必加入缓存
        log.info("新建用户的主键ID = {}", user1.getId());
    }

    @Test
    public void testR(){
        User user1 = userBasicService.getUserById(3);
        log.info("result = {}", JSON.toJSONString(user1));
        /**
         * TODO 此处#result.id总是无法获取到值，导致无法添加到缓存
         */
        User user2 = userBasicService.getUserByPhone("1886374");
        log.info("依据手机号查用户：{}",JSON.toJSONString(user2));
    }

    @Test
    public void testD(){
        int i = userBasicService.deleteUserByPhone("123678");
        log.info("主键是：{}", i);
    }

    /**
     * 更新时传递的User可能只有必要的字段，所以不建议更新缓存，否则总是要主动查一次
     */
    @Test
    public void testU(){
        User update = new User(3,null,"jacky",null);
        User user = userBasicService.updateUserById(update);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testRedis(){
        ValueOperations valueOps = redisTemplate.opsForValue();
        User user = new User("123678","jack",12);
        valueOps.set("user",user);
    }

}
