package com.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:04subpub-config.xml")
public class _05Expire {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        SessionCallback callback = new SessionCallback() {
            String keyName = "name";
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                BoundValueOperations boundValueOp = operations.boundValueOps(keyName);
                boundValueOp.set("jack");
                String keyValue = (String) boundValueOp.get();
                //>>>>>>>  ttl key
                long expireS = operations.getExpire(keyName);//-1
                //>>>>>>>  expire key seconds
                boolean processResult = operations.expire(keyName,120L, TimeUnit.SECONDS);
                //>>>>>>>  persist key
                processResult = operations.persist(keyName);//true
                Long timeDuration = operations.getExpire(keyName);//-1
                LocalDateTime expireSeconds = LocalDateTime.now().plusSeconds(120);
                //>>>>>>>  expireat key timestamp
                processResult = operations.expireAt(keyName, Date.from(expireSeconds.atZone(ZoneId.systemDefault()).toInstant()));
                return null;
            }
        };
        redisTemplate.execute(callback);
    }
}
