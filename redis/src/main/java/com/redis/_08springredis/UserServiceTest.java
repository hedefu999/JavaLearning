package com.redis._08springredis;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringMybatisConfig.class})
public class UserServiceTest {
    @Autowired
    private UserBasicService userBasicService;
    @Test
    public void test(){
        User userByPhone = userBasicService.getUserByPhone("123675");
        System.out.println(JSON.toJSONString(userByPhone));
    }
}
