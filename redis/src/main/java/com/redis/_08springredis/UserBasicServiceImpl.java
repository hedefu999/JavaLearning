package com.redis._08springredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("userBasicService")
public class UserBasicServiceImpl implements UserBasicService {
    private final Logger log = LoggerFactory.getLogger(UserBasicServiceImpl.class);
    @Autowired
    private UserRepo userRepo;

    @Override
    //使用SpEL声明缓存的key,方法必须将保存的role再返回回来
    //TODO 此处返回的role中id是数据库生成的无法填充
    @CachePut(value = "redisCacheManager",key = "'user-'+#result.id")
    public User saveUser(User user) {
        int i = userRepo.createUser(user);
        return user;
    }

    @Override
    @CachePut(value = "redisCacheManager",key = "'user-'+#result.id")
    public User updateUserById(User user) {
        int i = userRepo.updateUserById(user);
        return user;
    }

    @Override
    //此处应使用result.id,而非user.id
    @CachePut(value = "redisCacheManager",key = "'user-'+#result.id")
    public User updateUserByPhone(User user) {
        int i = userRepo.updateUserByPhone(user);
        //取保id不为空
        if (user.getId() == null){
            User user1 = userRepo.retrieveUserByPhone(user.getPhone());
            return user1;
        }
        return user;
    }

    @Override
    @Cacheable(value = "redisCacheManager", key = "'user-'+#id")
    public User getUserById(Integer id) {
        User user = userRepo.retrieveUserById(id);
        return user;
    }

    /**
     * 如果更新时传入的user的id为空，将导致缓存更新出错
     * @param phone
     * @return
     */
    @Override
    @Cacheable(value = "redisCacheManager",key = "'user-'+#result.id")
    public User getUserByPhone(String phone) {
        User user = userRepo.retrieveUserByPhone(phone);
        return user;
    }

    @Override
    @Cacheable(value = "redisCacheManager",key = "'user-'+#result.id")
    public int deleteUserById(Integer id) {
        int i = userRepo.deleteUserById(id);
        return i;
    }

    /**
     * 为确保缓存正确，会引入一些不必要的查询。。。
     * @param phone
     * @return
     */
    @Override
    @CacheEvict(value = "redisCacheManager",key = "'user-'+#result")
    public int deleteUserByPhone(String phone) {
        User user = userRepo.retrieveUserByPhone(phone);
        int i = userRepo.deleteUserByPhone(phone);

        return user.getId();
    }
}
