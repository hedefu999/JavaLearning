package com.redis._08springredis;

import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserBasicService {
    int saveUser(User user);
    User updateUserById(User user);
    User updateUserByPhone(User user);
    User getUserById(Integer id);
    User getUserByPhone(String phone);

    @CachePut(cacheNames = "user",key = "#result.id")
    User getUserByPhone(User user);

    int deleteUserById(Integer id);
    int deleteUserByPhone(String phone);


    @Transactional(propagation = Propagation.REQUIRED)
    int saveUsers(List<User> userList);
}
