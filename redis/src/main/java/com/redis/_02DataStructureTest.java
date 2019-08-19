package com.redis;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/02datastructure-config.xml")
public class _02DataStructureTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testBasicStringCommand(){
        System.out.println("");
        String key1 = "key1";
        String key2 = "key2";
        //>>>>> set key value
        redisTemplate.opsForValue().set(key1,"yetti");
        redisTemplate.opsForValue().set(key2,"mummy");
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //>>>>> get key
        //String value1 = (String) valueOperations.get(key1);
        //>>>>> del key
        redisTemplate.delete(key2);
        //>>>>> strlen key
        Long size = valueOperations.size(key1);
        System.out.println(size);
        //>>>>> getset key value
        Object oldValue2 = valueOperations.getAndSet(key2, "ghost");//null
        //>>>>> append key value
        valueOperations.append(key2," is sth frightening");
        //>>>>> getrange key start end
        String subStr = redisTemplate.opsForValue().get(key2, 1, 3);//从0开始会打印出双引号
        System.out.println(subStr);
    }
    //value字符串是数字时，redis支持简单的运算,不支持乘法、除法等操作
    //对浮点数进行
    @Test
    public void testSimpleCalc(){
        String numKey = "numKey";
        redisTemplate.opsForValue().set(numKey,123);
        //>>>>> decrby key value
        //>>>>> incrby key value
        redisTemplate.opsForValue().increment(numKey,2);
        //>>>>> incr key
        redisTemplate.opsForValue().increment(numKey);
        //>>>>> decr key
        redisTemplate.opsForValue().decrement(numKey);
        redisTemplate.opsForValue().get(numKey);//125
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //>>>>> incrbyfloat key floatvalue
        valueOperations.set("floatkey",23.2f);
        //todo 浮点数对应的javaAPI加法是什么？此处一直报错
        //valueOperations.increment("floatkey");出错
        //valueOperations.increment("floatkey",1.2f);
    }

    /**
     * redis的数据结构之 hash 键值对
     */
    @Test
    public void testRedisHash(){
        String hashName = "hash";
        Map<String,String> map = new HashMap<>();
        map.put("name","jack");
        map.put("age","12");
        HashOperations hashOperations = redisTemplate.opsForHash();
        //>>>>>  hmset key field1 value1 field2 value2...
        hashOperations.putAll(hashName,map);
        //>>>>> hset key field value
        hashOperations.put(hashName,"height","180");
        //>>>>> hexists key field
        boolean exists = hashOperations.hasKey(hashName,"height");
        //>>>>> hget key field
        Object value = hashOperations.get(hashName,"name");
        //>>>>> hmget key field1 field2
        List valList2 = hashOperations.multiGet(hashName,Lists.newArrayList("name","age"));
        //>>>>> hgetall key
        Map<String,String> keyValMap = redisTemplate.opsForHash().entries(hashName);
        //>>>>> hincrby key field increment
        hashOperations.increment(hashName,"height",2);
        //>>>>> hincrbyfloat key field increment
        hashOperations.increment(hashName,"height",0.2);
        //>>>>> hvals key
        List valList = hashOperations.values(hashName);
        //>>>>> hkeys key
        Set keyList = hashOperations.keys(hashName);
        //>>>>> hsetnx key field name
        boolean success = hashOperations.putIfAbsent(hashName,"address","sh");
        //>>>>> hdel key field1 field2
        Long result = hashOperations.delete(hashName,"age","height");

    }

    /**
     * redis数据结构之 链表 linked-list
     */
    @Test
    public void test2(){



    }
}
