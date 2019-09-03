package com.redis._08springredis;

public interface UserBasicService {
    User saveUser(User user);
    User updateUserById(User user);
    User updateUserByPhone(User user);
    User getUserById(Integer id);
    User getUserByPhone(String phone);
    int deleteUserById(Integer id);
    int deleteUserByPhone(String phone);


}
