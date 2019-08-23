package com.redis;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
    public void testList()throws Exception{
        String charset = "utf-8";
        String listName = "list";
        redisTemplate.delete(listName);
        ListOperations listOp = redisTemplate.opsForList();
        //>>>>>> lpush key node1
        listOp.leftPush(listName,"water");//water
        List<String> list = Lists.newArrayList("sky","life","socks");
        //插入完成后lpop出来的应该是 --- socks
        //>>>>>> lpush key node1 node2 ...
        listOp.leftPushAll(listName,list);//socks life sky water
        //>>>>> rpush key node1
        listOp.rightPush(listName,"macOS");//socks life sky water macOS
        //>>>>> lindex key index
        String zeroNode = (String) listOp.index(listName,0);
        //>>>>> llen key
        long size = listOp.size(listName);
        //>>>>> lpop key
        String lpop = (String) listOp.leftPop(listName);//life sky water macOS
        //>>>>> rpop key
        String rpop = (String) listOp.rightPop(listName);//life sky water

        //lInsert表示 List Insert
        //在sky前面life后面插入music
        //>>>>> linsert key before pivot node
        redisTemplate.getConnectionFactory().getConnection().lInsert(
                listName.getBytes(charset),
                RedisListCommands.Position.BEFORE,
                "sky".getBytes(charset),
                "music".getBytes(charset));//life music sky water
        //>>>>> linsert key after pivot node
        redisTemplate.getConnectionFactory().getConnection().lInsert(
                listName.getBytes(charset),
                RedisListCommands.Position.AFTER,
                "sky".getBytes(charset),
                "pinetree".getBytes(charset));//life music sky pinetree water
        //>>>>> lpushx list node
        listOp.leftPushIfPresent(listName,"head");//head life music sky pinetree water
        //>>>>> rpushx list node
        listOp.rightPushIfPresent(listName,"end");//head life music sky pinetree water end
        //rang命令可用于打印整个list
        //>>>>> lrange list start end
        List nodeList = listOp.range(listName,1,3);//life music sky
        //在链表左边插入3个值为node的结点
        listOp.leftPushAll(listName,Lists.newArrayList("node","node","node"));//node node node head life music sky pinetree water end
        //从左至右至多删除2个node节点
        //>>>>> lrem list count value
        listOp.remove(listName,2,"node");
        //>>>>> lset key index node
        listOp.set(listName,0,"new_head_value");//new_head_value head life music sky pinetree water end
        //>>>>> ltrim key start stop
        listOp.trim(listName,3,5);//music sky pinetree

        //一些线程安全的阻塞命令
        //>>>>> blpop key timeout;
        listOp.leftPop(listName,3, TimeUnit.SECONDS);
        //>>>>> brpop key timeout;
        listOp.rightPop(listName,2,TimeUnit.SECONDS);
        listOp.leftPushAll("list2","jeans","trousers","skirt");
        //>>>>> brpoplpush src dest timeout
        listOp.rightPopAndLeftPush("list2",listName,4,TimeUnit.MILLISECONDS);//jeans sky
    }
    /**
     * redis数据结构之 集合
     */
    @Test
    public void testSet(){
        String america = "america";
        String china = "china";
        //>>>>>> sadd key item1 item2 ...;
        redisTemplate.boundSetOps(america).add("washington","new york","hawaii","bolston");
        redisTemplate.boundSetOps(china).add("jiangsu","shanghai","beijing","hawaii");
        SetOperations setOp = redisTemplate.opsForSet();
        //>>>>>> scard key
        Long size = setOp.size(america);
        //>>>>>> sdiff key key2
        Set diff = setOp.difference(america, china);
        //>>>>>> sinter key1 key2
        Set inter = setOp.intersect(america, china);
        //>>>>>> sunion key1 key2
        Set union = setOp.union(america, china);
        //>>>>>> sismember key member
        Boolean bool = setOp.isMember(america, "tiananmen");
        //>>>>>> smembers key
        Set members = setOp.members(china);
        //>>>>>> spop key 随机弹出一个元素
        String pop = (String) setOp.pop(china);
        //>>>>>> srandmember key count
        String random = (String) setOp.randomMember(america);
        //>>>>>> srandmember key count 随机弹出count个，元素会重复弹出
        List list = setOp.randomMembers(america, 3);
        //>>>>>> srem key mem1 mem2 ...
        Long remove = setOp.remove(china, "hawaii");
        //>>>>>> sunionstore des key1 key2
        Long union1 = setOp.unionAndStore(america, china, "union");
    }
    /**
     * redis数据结构之 有序集合
     */
    @Test
    public void testZSet(){
        String zsetName = "zsetName";
        String zsetNameSameScore = "zsetNameSameScore";
        Boolean delete = redisTemplate.delete(zsetName);
        redisTemplate.delete(zsetNameSameScore);

        String[] school = {"primary","middle","high","bachelor","graduate","doctor","researcher"};
        ZSetOperations zSetOp = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple> set = new HashSet<>();
        Set<TypedTuple> setSameScore = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            //注意有序结合的score是双精度浮点数，不是整数，虽然redis命令里看不出来
            Double score = Double.valueOf(i);
            TypedTuple schooolTuple = new DefaultTypedTuple(school[i],score);
            TypedTuple schoolSameScoreTuple = new DefaultTypedTuple(school[i],Double.valueOf(1));
            set.add(schooolTuple);
            setSameScore.add(schoolSameScoreTuple);
        }
        //>>>>>>> zadd key score1 value1 score2 value2 ...;
        zSetOp.add(zsetName,set);
        zSetOp.add(zsetNameSameScore,setSameScore);
        //>>>>>>> zcard key
        Long size = zSetOp.zCard(zsetName);
        //求score在[3,6]区间内的元素的个数
        //>>>>>>> zcount key start end
        Long count = zSetOp.count(zsetName, 3, 6);
        //截取元素,score在[1,5]区间中的元素
        //>>>>>>> zrangebyscore key start end
        Set<String> itemSet = zSetOp.range(zsetName, 1, 5);
        //获取所有元素，包含分数，并按分数排序
        //>>>>>>> zrangebyscore key min max withscores;
        Set<TypedTuple> set1 = zSetOp.rangeWithScores(zsetName, 0, -1);
        //新zset中的socre来自第一个zset zsetName
        //>>>>>>> zinterstore destkey count key1 key2 ...;
        Long newSetSize = zSetOp.intersectAndStore(zsetName, zsetNameSameScore, "newZset");

        RedisZSetCommands.Range range = Range.range();
        //>>>>>>>> zrangebylex  lexset (ehi (mm
        range.gt("ehi");range.lt("mm");
        Set<String> lexSet = zSetOp.rangeByLex(zsetNameSameScore, range);
        /**0
         * graduate
         * 1
         * high
         * 2
         * middle
         */

        //>>>>>>>> zrangebylex  lexset [ach [st
        range.gte("ach");range.lte("st");
        Set<String> lexSet2 = zSetOp.rangeByLex(zsetNameSameScore, range);
        /**0
         * bachelor
         * 1
         * doctor
         * 2
         * graduate
         * 3
         * high
         * 4
         * middle
         * 5
         * primary
         * 6
         * researcher
         */
        RedisZSetCommands.Limit limit = Limit.limit();
        limit.offset(2);limit.count(4);//跳过两个元素取4个
        //>>>>>>> zrangebyscore key min max withscores limit offset count;
        Set<String> lexLimitSet = zSetOp.rangeByLex(zsetName, range, limit);
        //middle的排名，从0开始计
        //>>>>>>> zrank key member;
        Long middleRank = zSetOp.rank(zsetName, "middle");
        //>>>>>>> zrangebyscore key min max withscores;
        Set<String> allItems = zSetOp.rangeWithScores(zsetName, 0, -1);
        //>>>>>>> zrem key member
        Long deleteCount = zSetOp.remove(zsetName, "researcher");
        //>>>>>>> zremrangebyscore key start stop;
        Long removeCount = zSetOp.removeRange(zsetName, 1, 3);
        //>>>>>>> zincrby key increment member
        Double result = zSetOp.incrementScore(zsetName, "primary", 10);
        //>>>>>>> zrevrangebyscore key max min;
        Set<String> revSet = zSetOp.reverseRangeByScore(zsetName, 2, 8);
    }

    /**
     * 测试redis的基数hyperLogLog
     */
    @Test
    public void testHyperLog(){
        String hyperlog1 = "hyperlog1";
        String hyperlog2 = "hyperlog2";
        String hyperlog3 = "hyperlog3";
        HyperLogLogOperations hyperLogOp = redisTemplate.opsForHyperLogLog();
        //基数元素都是String
        //>>>>>> pfadd key element
        Long count = hyperLogOp.add(hyperlog1, "a", "b","1", "s", "b","1","3");//count = 1
        Long count2 = hyperLogOp.add(hyperlog2, "a", "s", "z");//count2 = 1
        //>>>>>>  pfcount key
        Long hyperlog1size = hyperLogOp.size(hyperlog1);//5
        Long hyperlog2size = hyperLogOp.size(hyperlog2);//3
        //>>>>>>  pfmerge destkey key1 key2...
        Long union = hyperLogOp.union(hyperlog3, hyperlog1, hyperlog2);//6: asb1z3 union=6
        Long mergeSize = hyperLogOp.size(hyperlog3);//6


    }
}
