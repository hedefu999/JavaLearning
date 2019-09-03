package com.redis._08springredis;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo {
    int createUser(User user);
    User retrieveUserById(Integer id);
    User retrieveUserByPhone(String phone);
    int updateUserById(User user);
    int updateUserByPhone(User user);
    int deleteUserById(Integer id);
    int deleteUserByPhone(String phone);
}
