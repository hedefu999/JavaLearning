package com.redis;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/03transaction-config.xml")
public class _03TransactionTest {
    private final Logger log = LoggerFactory.getLogger(_03TransactionTest.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis的事务操作测试
     */
    @Test
    public void testTransaction(){
        SessionCallback callback = new SessionCallback<String>() {
            String keyName = "key1";
            @Override
            public <K,V> String execute(RedisOperations<K,V> ops) throws DataAccessException {
                ops.multi();
                BoundValueOperations<K,V> boundKeyOp = ops.boundValueOps((K) keyName);
                boundKeyOp.set((V) "value1");//1.
                boundKeyOp.append(" is settled by redis");//2.
                BoundValueOperations boundValueOp = ops.boundValueOps((K) keyName);
                String value = (String) boundValueOp.get(); //此时命令尚未执行，故value为null,3
                //list中保存之前所有命令的结果，分别对应上面3行指令的执行结果。26指的是整个value的长度
                List<Object> execList = ops.exec();//[true,26,"value1 is settled by redis"]

                //exec之后才能获取到
                ValueOperations valueOp = redisTemplate.opsForValue();
                value = (String) valueOp.get(keyName);

                return value;
            }
        };
        String value = (String) redisTemplate.execute(callback);
        log.info("获取所设置的值：{}。",value);
    }

    /**
     * redis的事务回滚
     * 1.命令格式本身错误时
     * -----------
     * > multi
     * > set key1 value1 //A
     * queued
     * > incr
     * error
     * > exec
     * -----------
     * A处的命令不能正常执行，命令本身错误
     *
     * 2.命令操作的数据结构错误时
     * ---------------
     * > multi
     * OK
     * > set key1 value1 //A
     * QUEUED
     * > incr key1
     * QUEUED
     * > set key2 value2 //B
     * > exec
     * 1>OK
     * 2>error
     * 3>OK
     * ---------------
     * 执行命令遇到数据格式错误，A B处的指令能正常执行
     */
    public void testTxRollback(){

    }

    /**
     * 使用流水线pipedline提高性能
     * 使用JavaApi比redisTemplate的速度要快
     */
    @Autowired
    private JedisPool jedisPool;
    @Test
    public void testJavaPipelinedAPI(){
        Jedis jedis = jedisPool.getResource();
        Pipeline pipelined = jedis.pipelined();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            pipelined.set("key"+i,"value"+i);
        }
        //pipelined.sync(); 仅执行，但不返回结果
        List<Object> result = pipelined.syncAndReturnAll();//返回执行的结果，但list可能撑爆内存
        System.out.println(JSON.toJSONString(result));//100个OK
        System.out.println(System.currentTimeMillis()-start);//21ms
    }
    @Test
    public void testRedisTemplatePipedLined(){
        SessionCallback callback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (int i = 0; i < 100; i++) {
                    operations.boundValueOps("key"+i).set("value"+i);
                }
                return null;
            }
        };
        long start = System.currentTimeMillis();
        List resultList = redisTemplate.executePipelined(callback);
        System.out.println(JSON.toJSONString(resultList));//100个true
        System.out.println(System.currentTimeMillis()-start);//93ms
    }

}
