package com.ssmr.c12;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/ssmr/c12/spring-mybatis.xml")
public class MybatisSpring {
    @Autowired(required = false)
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * sqlSessionTemplate也能拿来CRUD，但运用不多
     */
    @Test
    public void test(){
        String statement = "com.ssmr.c12.RoleMapper.getRole";
        Integer param_id = 3;
        Role role = sqlSessionTemplate.selectOne(statement, param_id);
        System.out.println(JSON.toJSONString(role));
        //role.setNote("fake role");
        //String statement2 = "com.ssmr.c12.RoleMapper.saveEntireRole";
        //Integer result = sqlSessionTemplate.insert(statement2,role);
        //System.out.println(result);//-2147482646  不知道返回的是个啥
    }
    @Autowired
    private RoleMapper roleMapper;
    @Test
    public void testMapperInject(){
        //同样的，每使用一个Mapper接口的方法，都会创建一个新的SqlSession
        Role role = roleMapper.getRole(2);
        System.out.println(JSON.toJSONString(role));
    }
}
