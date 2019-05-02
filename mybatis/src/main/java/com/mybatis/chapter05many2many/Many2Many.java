package com.mybatis.chapter05many2many;

import com.mybatis.chapter05many2many.mapper.ProcedureMapper;
import com.mybatis.chapter05many2many.mapper.RoleMapper;
import com.mybatis.chapter05many2many.mapper.UserMapper;
import com.mybatis.chapter05many2many.mapper.UserRoleMapMapper;
import com.mybatis.chapter05many2many.model.ProcedureModel;
import com.mybatis.chapter05many2many.model.Role;
import com.mybatis.chapter05many2many.model.User;
import com.mybatis.chapter05many2many.model.UserRoleMap;
import com.mybatis.utils.JSONUtils;
import com.mybatis.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

public class Many2Many {
    @Test
    public void testUserRoles(){
        UserMapper mapper = SqlSessionFactoryUtils.getMapper(UserMapper.class,
                "com/mybatis/chapter05many2many/mybatis-config.xml");
        User user = mapper.getUserRoles(1);
        System.out.println(JSONUtils.toString(user));
    }
    @Test
    public void testRoleUsers(){
        RoleMapper mapper = SqlSessionFactoryUtils.getMapper(RoleMapper.class,
                "com/mybatis/chapter05many2many/mybatis-config.xml");
        Role role = mapper.getRoleUsers(1);
        System.out.println(JSONUtils.toString(role));
    }
    @Test
    public void testCache(){
        SqlSessionFactory factory =
                SqlSessionFactoryUtils.getSqlSessionFactory("com/mybatis/chapter05many2many/mybatis-config.xml");
        SqlSession session0 = factory.openSession();
        UserRoleMapMapper mapper0 = session0.getMapper(UserRoleMapMapper.class);
        UserRoleMap userRoleMap1 = mapper0.getById(1);
        session0.commit();

        userRoleMap1.setRoleId(1);
        mapper0.updateUserRoleMapById(userRoleMap1);
        session0.commit();

        SqlSession session2 = factory.openSession();
        UserRoleMapMapper mapper2 = session0.getMapper(UserRoleMapMapper.class);
        mapper2.getById(1);
        session2.commit();
    }
    @Test
    public void testProcedure(){
        ProcedureMapper mapper  =SqlSessionFactoryUtils.getMapper(ProcedureMapper.class,
                "com/mybatis/chapter05many2many/mybatis-config.xml");
        ProcedureModel model = new ProcedureModel(2,null);
        mapper.callPrimaryProc(model);
        System.out.println(model);
    }
}
