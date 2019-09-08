package com.redis._09practicalredistemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * redisTemplate每执行一个方法都会从redis连接池获取一条连接
 * 使用SessionCallback可以使得所有操作来自同一条redis连接，避免命令在不同连接上执行
 * 但使用事务或流水线时，执行的结果不能马上返回。流水线的作用是将所有指令一次发送到redis服务器，避免网络拖慢redis执行速度
 */
@Service("iRedisTemplate")
public class RedisTemplateServiceImpl implements AdvancedRedisTemplateService {
    @Autowired
    private RedisTemplate redisTemplate = null;
    @Override
    public void execMultiCommand() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.boundValueOps("key1").set("abc");
                operations.boundHashOps("hash").put("hash-key-1","hash-value-1");
                return operations.boundValueOps("key1").get();
            }
        });
        System.out.println(obj);//abc
    }

    @Override
    public void execTransaction() {
        List list = (List) redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.watch("key2");
                operations.multi();
                operations.boundValueOps("key2").set("ab2c");
                operations.boundHashOps("hash").put("hash-key-2","hash-value-2");
                operations.opsForValue().get("key2");
                List result = operations.exec();
                return result;
            }
        });
        System.out.println(list);//[true, true, ab2c]
    }

    @Override
    public void execPipeline() {
        List list = redisTemplate.executePipelined(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations operations) throws DataAccessException {
                //在流水线模式下，命令不会马上返回结果，结果是一次性执行后返回的
                operations.opsForValue().set("key3","value3");
                operations.opsForHash().put("hash","hash-key-3","hash-value-3");
                operations.opsForValue().get("key3");
                /**
                 * 此处必须返回null，返回非空反而会报错 Callback cannot return a non-null value as it gets overwritten by the pipeline
                 * RedisTemplate Line286 对返回结果进行了判断，返回非空会抛异常
                 */
                return null;
            }
        });
        System.out.println(list);//[true, false, value3]
    }
}
