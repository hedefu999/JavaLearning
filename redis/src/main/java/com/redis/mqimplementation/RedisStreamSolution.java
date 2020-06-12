package com.redis.mqimplementation;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.jedis.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisStreamSolution {
    private static final Logger log = LoggerFactory.getLogger(RedisStreamSolution.class);

    private static final String _STREAM_KEY = "mq"; // 流 key
    private static final String _GROUP_NAME = "g1"; // 分组名称
    private static final String _CONSUMER_NAME = "c1"; // 消费者 1 的名称
    private static final String _CONSUMER2_NAME = "c2"; // 消费者 2 的名称

    public static void main(String[] args) throws InterruptedException {
        producer();//生产者
        //创建消费者
        createGroup(_STREAM_KEY, _GROUP_NAME);
        new Thread(() -> consumer()).start();
        new Thread(() -> consumer2()).start();
    }
    public static Jedis getJedis(){
        return new Jedis("127.0.0.1",6379);
    }

    public static void createGroup(String stream, String groupName){
        Jedis jedis = getJedis();
        //redis2.x的版本不支持
        jedis.xgroupCreate(stream, groupName, new StreamEntryID(), true);
    }

    //生产者
    public static void producer(){
        Jedis jedis = getJedis();
        //添加消息1
        Map<String,String> map = new HashMap<>();
        map.put("data","redis");
        StreamEntryID id = jedis.xadd(_STREAM_KEY,null,map);
        log.info("消息添加成功，ID = {}", id);
        //添加消息2
        Map<String, String> map2 = new HashMap<>();
        map2.put("data","java");
        StreamEntryID id2 = jedis.xadd(_STREAM_KEY, null, map2);
        log.info("消息添加成功, ID = {}", id2);
    }
    //消费者1
    public static void consumer(){
        Jedis jedis = getJedis();
        while (true){
            //读取消息
            Map.Entry<String, StreamEntryID> entry =
                    new AbstractMap.SimpleImmutableEntry<>(_STREAM_KEY, new StreamEntryID().UNRECEIVED_ENTRY);
            //阻塞读取一条消息，最大阻塞时间120s   【未知原因 - 这两处会很快报 read timed out异常，貌似是bug】
            //noack参数表示是否自动确认消息
            List<Map.Entry<String, List<StreamEntry>>> list =
                    jedis.xreadGroup(_GROUP_NAME, _CONSUMER_NAME,1,120*1000,true,entry);
            if (list != null && list.size() == 1){
                //读取到消息
                Map<String,String> content = list.get(0).getValue().get(0).getFields();
                log.info("consumer1 读取到消息：id = {}, content = {}.",
                        list.get(0).getValue().get(0).getID(),new Gson().toJson(content));
            }
        }
    }

    //消费者2
    public static void consumer2(){
        Jedis jedis = getJedis();
        while (true){
            //读取消息
            Map.Entry<String, StreamEntryID> entry =
                    new AbstractMap.SimpleImmutableEntry<>(_STREAM_KEY, new StreamEntryID().UNRECEIVED_ENTRY);
            //阻塞读取一条消息，最大阻塞时间120s
            List<Map.Entry<String, List<StreamEntry>>> list =
                    jedis.xreadGroup(_GROUP_NAME, _CONSUMER2_NAME,1,120*1000,true,entry);
            if (list != null && list.size() == 1){
                //读取到消息
                Map<String,String> content = list.get(0).getValue().get(0).getFields();
                log.info("consumer2 读取到消息：id = {}, content = {}.",
                        list.get(0).getValue().get(0).getID(),new Gson().toJson(content));
            }
        }
    }
    /**
     * 同一个分组内的多个consumer会读取到不同消息，不同的consumer不会读取到分组内的同一条消息
     */
}
