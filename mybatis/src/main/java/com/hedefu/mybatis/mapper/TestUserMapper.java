package com.hedefu.mybatis.mapper;

import com.hedefu.mybatis.model.TestUser;

public interface TestUserMapper {
    int insertTestUser(TestUser testUser);

    TestUser getTestUserById(Integer id);

    TestUser getTestUserByName(String name);

    String getUserTypeByName(String name);
}
