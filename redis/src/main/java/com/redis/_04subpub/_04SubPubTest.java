package com.redis._04subpub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:04subpub-config.xml")
//@ComponentScan("com.redis._04subpub")
public class _04SubPubTest {
    @Autowired
    private RedisMessageListener redisMessageListener;
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    public void testSubPub(){
        String channelName = "chatChannel";
        redisTemplate.convertAndSend(channelName,"redis message sended by junit with spring");
        //正在监听的客户就能收到这条消息了
    }
}
