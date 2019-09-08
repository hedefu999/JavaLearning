package com.redis._08springredis;

import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserBasicService {
    User saveUser(User user);
    User updateUserById(User user);
    User updateUserByPhone(User user);
    User getUserById(Integer id);
    User getUserByPhone(String phone);

    User getUserByPhone(User user);

    int deleteUserById(Integer id);
    int deleteUserByPhone(String phone);

    int saveUsers(List<User> userList);
}
