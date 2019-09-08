package com.redis._08springredis;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringMybatisConfig.class})
@ComponentScan("com.redis._08springredis")
public class TestUserDAO {
    private final Logger log = LoggerFactory.getLogger(TestUserDAO.class);
    private User user = null;
    @Autowired
    private UserRepo userRepo;

    @Before
    public void init(){
        user = new User("384765","gradu",13);
    }
    @Test
    public void test(){
        int user1 = userRepo.createUser(user);
        System.out.println(user1);
    }
    @Test
    public void test2(){
        //User user = userRepo.retrieveUserByPhone("123678");
        //user = userRepo.retrieveUserById(1);
        user.setAge(13);
        //userRepo.updateUserByPhone(user);
        user.setPhone("1255637");
        user.setId(1);
        userRepo.updateUserById(user);
        log.info("user = {}", JSON.toJSONString(user));
    }
    @Test
    public void test3(){
        //userRepo.deleteUserByPhone("1255637");
        //userRepo.deleteUserById(1);
        userRepo.deleteUserById(2);
    }
}
