package com.redis._08springredis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

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
        User user = new User("893564","htgx",38);
        User result = userBasicService.saveUser(user);
        log.info("新建用户的主键ID = {}", result);
    }

    @Test
    public void testR(){
        User user1 = userBasicService.getUserById(18);
        log.info("result = {}", JSON.toJSONString(user1));
        //User user2 = userBasicService.getUserByPhone("143209");
        //log.info("依据手机号查用户：{}",JSON.toJSONString(user2));

    }
    @Test
    public void testR2(){
        User query = new User();
        query.setPhone("143209");
        query.setId(3);
        userBasicService.getUserByPhone(query);
    }

    @Test
    public void testU(){
        User user = new User(18,"45与45","jacky23",12);
        userBasicService.updateUserById(user);
        //User user1 = new User("1886374","hashou",22);
        //userBasicService.updateUserByPhone(user1);
    }

    @Test
    public void testD(){
        int i = userBasicService.deleteUserByPhone("8376432");
        log.info("主键是：{}", i);
        //userBasicService.deleteUserById(9);
    }

    @Test
    public void testBatC(){
        User user1 = new User("876325","fdg",34);
        User user2 = new User("657843","yhkmty",54);
        List<User> userList = Arrays.asList(user1, user2);
        userBasicService.saveUsers(userList);
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
