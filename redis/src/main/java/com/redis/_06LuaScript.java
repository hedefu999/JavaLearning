package com.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:04subpub-config.xml")
public class _06LuaScript {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JedisConnectionFactory connectionFactory;
    @Test
    public void test(){
        Jedis jedisNativeConn = (Jedis) connectionFactory.getConnection().getNativeConnection();
        //执行简单Lua脚本
        //>>>>>>>>>  eval "return 'hello java'" 0
        String result = (String) jedisNativeConn.eval("return 'hello java'");//hello java
        //执行带有参数的Lua脚本
        //>>>>>>>>>  eval "redis.call('set',KEYS[1],ARGV[1])" 1 lua-key lua-value
        result = (String) jedisNativeConn.eval("redis.call('set',KEYS[1],ARGV[1])",1,"age","13");//null

        String age = jedisNativeConn.get("age");//13

        //将lua脚本缓存起来，获得32为sha-1码
        //>>>>>>>>>  script load "redis.call('set',KEYS[1],ARGV[1])"
        String scriptSHA1 = jedisNativeConn.scriptLoad("redis.call('set',KEYS[1],ARGV[1])");//7cfb4342127e7ab3d63ac05e0d3615fd50b45b06
        String[] params = {"address","sh"};
        //通过缓存脚本的识别码执行脚本
        //>>>>>>>>>  evalsha 7cfb4342127e7ab3d63ac05e0d3615fd50b45b06 1 name jack
        jedisNativeConn.evalsha(scriptSHA1,1,params);

        String address = jedisNativeConn.get("address");//sh

        //关闭连接
        jedisNativeConn.close();
    }

    @Autowired
    private JdkSerializationRedisSerializer jdkSerializer;
    @Autowired
    private GenericFastJsonRedisSerializer fastJsonSerializer;

    /**
     * 测试spring提供的DefaultRedisScript，将对象Role序列化保存到key为role1的缓存中
     */
    @Test
    public void testDefaultRedisScript(){
        String luaScript = "redis.call('set',KEYS[1],ARGV[1]) return redis.call('get',KEYS[1])";
        List<String> keyList = new ArrayList<>();
        keyList.add("role1");
        Role role = new Role();
        role.setId(12);role.setAge(12);role.setName("jack");role.setNote("test data");

        DefaultRedisScript<Role> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Role.class);
        String scriptSHA1 = redisScript.getSha1();

        //入参情况 RedisScript<T> script, RedisSerializer<?> argsSerializer, resultSerializer, List<K> keys, Object... args
        Role result = (Role) redisTemplate.execute(redisScript, fastJsonSerializer, fastJsonSerializer, keyList, role);
    }
    /**
     * 直接执行lua脚本文件
     * 使用命令： redis-cli --eval /Users/hedefu/Documents/DEVELOPER/IDEA/JavaLearning/redis/src/main/resources/06lua-script.lua year month , 2019 8
     * 注意上述命令中的逗号,左右的空格不能省略
     *
     * 使用java：java无法执行上面的指令，只能通过Jedis的eval/evalsha函数执行lua脚本，推荐使用evalsha,可以避免网络传输慢带来的问题
     */
    @Test
    public void testReadLuaScriptFile() throws Exception{
        File file = new File("src/main/resources/06lua-script.lua");
        //将lua脚本文件转为二进制数组
        byte[] bytes = new byte[(int) file.length()];
        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bb = new byte[2048];
        int ch = is.read(bb);
        while (ch != -1){
            baos.write(bb,0,ch);
            ch = is.read(bb);
        }
        bytes = baos.toByteArray();

        Jedis jedisNativeConn = (Jedis) connectionFactory.getConnection().getNativeConnection();
        byte[] sha1 = jedisNativeConn.scriptLoad(bytes);
        //使用返回的sha1表示执行命令,命令入参：byte[] sha1, int keyCount, byte[]... params
        Object result = jedisNativeConn.evalsha(sha1, 2, "origin".getBytes(), "new".getBytes(), "23".getBytes(), "12".getBytes());
        System.out.println(result);
    }

}
