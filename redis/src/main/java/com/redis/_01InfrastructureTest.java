package com.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:01infratructure-config.xml")
public class _01InfrastructureTest {
    private Role role = new Role();
    @Before
    public void init(){
        role.setId(12);
        role.setName("jack");
        role.setAge(12);
        role.setNote("hello world");
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /** 01.
     * 演示如果使用StringRedisSerializer序列化redis的key，使用JdkSerializationRedisSerializer序列化redis的value
     */
    @Test
    public void test(){
        //opsForValue的意思是Returns the operations performed on simple values
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("first_role",role);
        //下面两种方式都可以获取存入的first_role
        System.out.println(((Role)valueOperations.get("first_role")).getName());
            //再调一次opsForValue会获取新的redis连接
        Role role1 = (Role) redisTemplate.opsForValue().get("first_role");
        System.out.println(role1.getName());
    }

    /** 02.
     * 上面redisTemplate.opsForValue()总会获取新的ValueOperations
     * 上述get和set方法可能来自于同一个RedisPool的不同redis连接
     * 为了使得所有操作来自同一个redis连接，可以使用SessionCallback或RedisCallback接口
     */
    @Test
    public void test2(){
        SessionCallback<Role> sessionCallback = new SessionCallback<Role>() {
            @Override
            public  Role execute(RedisOperations operations) throws DataAccessException {
                operations.boundValueOps( "role_1").set( role);
                return (Role) operations.boundValueOps( "role_1").get();
            }
        };
        Role savedRole = (Role) redisTemplate.execute(sessionCallback);
        System.out.println(savedRole.getName());
        /**
         * 使用execute方法传入的RedisOperations就可以保证redis操作使用的是同一个redis连接，节约资源实现事务
         * 这样set和get命令就能保证在同一个连接池的同一个Redis连接进行操作 P493
         */
    }

}
