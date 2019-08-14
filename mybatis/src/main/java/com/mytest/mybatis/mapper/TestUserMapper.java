package com.mytest.mybatis.mapper;

import com.mytest.mybatis.model.TestUser;

public interface TestUserMapper {
    int insertTestUser(TestUser testUser);

    TestUser getTestUserById(Integer id);

    TestUser getTestUserByName(String name);

    String getUserTypeByName(String name);
}
