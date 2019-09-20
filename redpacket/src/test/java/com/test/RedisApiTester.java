package com.test;

import com.redpacket.FileUtils;
import com.redpacket.config.RedisConfig;
import com.redpacket.config.RootConfig;
import com.redpacket.config.WebAppInitializer;
import com.redpacket.config.WebConfig;
import org.apache.commons.io.output.StringBuilderWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, RedisConfig.class})
@ComponentScan(basePackages = "com.redpacket.*",
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class, RestController.class}),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebAppInitializer.class, WebConfig.class})
    })
public class RedisApiTester {
    private final Logger log = LoggerFactory.getLogger(RedisApiTester.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis01(){
        HashOperations hashOps = redisTemplate.opsForHash();
        hashOps.put("red_packet_1","stock",200);
        hashOps.put("red_packet_1","unit_amount",100);
    }
    @Autowired
    private RedisConnectionFactory redisConnFactory;

    //仿照commons.io写的readFileToString
    private String readFileToString(String filepath){
        String luaScriptStr = null;
        try (InputStream is = new FileInputStream(new File(filepath))){
            try (StringBuilderWriter builderWriter = new StringBuilderWriter()){
                InputStreamReader streamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
                int count = 0,EOF = -1;
                char[] DEFAULT_BUFFER = new char[4*1024];
                while (EOF != (count = streamReader.read(DEFAULT_BUFFER))){
                    builderWriter.write(DEFAULT_BUFFER,0,count);
                }
                luaScriptStr = builderWriter.toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return luaScriptStr;
    }

    @Test
    public void testLuaScript(){
        RedisConnection connection = redisConnFactory.getConnection();
        Jedis nativeConn = (Jedis) connection.getNativeConnection();
        String filepath = "src/main/resources/lua/grab_red_packet.lua";
        String luaScriptStr = readFileToString(filepath);
        String luaSHA = nativeConn.scriptLoad(luaScriptStr);
        //sha = 8b29c3b1f41e7721fd8e4aee00f58366e32d5c5b
        log.info("加载的这个lua脚本sha = {}", luaSHA);
        String userId = "2";
        String redpacketId = "1";
        nativeConn.evalsha(luaSHA,1,redpacketId, userId+System.currentTimeMillis());
    }

    @Test
    public void testRedisListInsert(){
        BoundListOperations redpacketListOps =
                redisTemplate.boundListOps("red_packet_list_1");
        log.info("列表大小：{}", redpacketListOps.size());
        List range = redpacketListOps.range(0, 3);
        log.info("0--3的列表内容：{}", range);
    }

    @Test
    public void testLuaScriptExecute(){
        String luaScript = FileUtils.readClassPathFileToString("lua/grab_red_packet.lua");
        //a2a4f70013b19b27b3ed23324869fa01d8998d79
    }

    @Test
    public void testRedisListOps(){
        BoundListOperations ops = redisTemplate.boundListOps("test_list");
        List range = ops.range(0, 3);
        System.out.println(range);
    }
}
