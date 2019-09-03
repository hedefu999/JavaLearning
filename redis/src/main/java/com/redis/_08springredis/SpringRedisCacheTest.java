package com.redis._08springredis;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SpringRedisCacheTest {
    private final Logger log = LoggerFactory.getLogger(SpringRedisCacheTest.class);
    @Autowired
    private UserBasicService userBasicService;

    @Test
    public void test(){
        //User user = new User("123678","jack",12);
        User user1 = userBasicService.getUserById(5);
        log.info("result = {}", JSON.toJSONString(user1));
    }

}
