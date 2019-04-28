package com.hedefu.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hedefu.mybatis.mapper.UserInfoMapper;
import com.hedefu.mybatis.model.UserInfo;
import com.hedefu.mybatis.qto.UserInfoQto;
import com.mybatis.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chapter05Test {
    private Logger log = LoggerFactory.getLogger("Chapter05Test");
    public UserInfoMapper getMapper(){
        return SqlSessionFactoryUtils.getMapper(UserInfoMapper.class,"mybatis-config.xml");
    }
    @Test
    public void test(){
        UserInfoMapper mapper = getMapper();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userName","jack");
        paramMap.put("age",12);
        UserInfo userInfo = mapper.getUserInfoByQueryMap(paramMap);
        System.out.println(userInfo);
    }
    @Test
    public void testGetUserInfoWithQTO(){
        UserInfoMapper mapper = getMapper();
        UserInfoQto qto = new UserInfoQto();
        qto.setName("lucy");
        qto.setAge(12);
        UserInfo userInfo = mapper.getUserInfoByQto(qto);
        System.out.println(userInfo);
    }

    @Test
    public void testRowBounds(){
        UserInfoMapper mapper = getMapper();
        int pageSize = 2,pageIndex = 2;
        RowBounds rowBounds = new RowBounds((pageIndex - 1)*pageSize,pageSize);
        List<UserInfo> userInfos = mapper.getByRowBounds(12,rowBounds);
        System.out.println(JSON.toJSONString(userInfos, SerializerFeature.WriteNullStringAsEmpty));
    }

    @Test
    public void testInsert(){
        //SqlSession session = SqlSessionFactoryUtils.openSqlSession();
        //UserInfo userInfo = UserInfo.getInstantce("sufe7",23,
        //        "be loyal to master", LocalDate.parse("2020-12-20"),
        //        new BigDecimal("0.000"), LocalDateTime.parse("2019-04-13T12:00:00"),LocalDateTime.parse("2019-04-20T12:00:00"));
        //UserInfoMapper mapper = session.getMapper(UserInfoMapper.class);
        ////int result = mapper.insertUserInfo(userInfo);
        //int result = mapper.insertUserInfoWithCustomizedKeyGenRule(userInfo);
        //session.commit();
        //session.close();
        //log.info("insert result={}, userInfo.id={}",result,userInfo.getId());
    }
    @Test
    public void testComplicatedResultMap(){
        UserInfoMapper mapper = getMapper();
        UserInfoQto qto = new UserInfoQto();
        qto.setName("lucy");
        qto.setAge(12);
        UserInfo userInfo = mapper.getByComplicatedResultMap(qto);
        log.info("查询结果: {}",userInfo);

    }

}
