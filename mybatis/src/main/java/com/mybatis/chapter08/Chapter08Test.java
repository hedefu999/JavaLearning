package com.mybatis.chapter08;

import com.google.common.collect.Lists;
import com.hedefu.mybatis.mapper.RoleMapper;
import com.hedefu.mybatis.model.Role;
import com.mybatis.utils.SqlSessionFactoryUtils;
import org.junit.Test;

import java.util.List;

public class Chapter08Test {
    @Test
    public void simpleQuery(){
        RoleMapper roleMapper = SqlSessionFactoryUtils.getMapper(RoleMapper.class,"mybatis-config.xml");
        PageParam pageParam = new PageParam(1,3,true,true,true,null,null);
        List<Role> roleList = roleMapper.findRoles(pageParam,Lists.newArrayList("teacher","saler",
                "professor",
                "principal",
                "cashier",
                "accoutant"
        ));
        System.out.println(roleList);
        //Role role = roleMapper.queryWithConvertedColumn(1L);
        //System.out.println(role);
    }
    @Test
    public void testPagePlugin2(){
        RoleMapper roleMapper = SqlSessionFactoryUtils.getMapper(RoleMapper.class,"mybatis-config.xml");
        PageParam pageParam = new PageParam(null,null,true,true,true,null,null);
        List<Role> roles = roleMapper.getRoles4Plugin(pageParam,2);
        System.out.println(roles);
    }
    @Test
    public void test0(){
        String sql = "select * from aaa orer by time";
        int index = sql.indexOf("order by");
        System.out.println(sql.substring(0,index));
    }
}
