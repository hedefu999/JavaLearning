package com.redis._08springredis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
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
    public User updateUserById(User user) {
        int i = userRepo.updateUserById(user);
        return user;
    }

    @Override
    //此处应使用result.id,而非user.id
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
    @Cacheable(cacheNames = "user", key = "#id")
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
    @Cacheable(cacheNames = "user",key = "#result.id",condition = "#result.id!=null")
    public User getUserByPhone(String phone) {
        User user = userRepo.retrieveUserByPhone(phone);
        log.info("查询到用户：{}",user);
        return user;
    }

    /**
     * 使用缓存时需要注意返回的int究竟是主键还是操作的行数
     */
    @Override
    @CacheEvict(cacheNames = "user",key = "#id")
    public int deleteUserById(Integer id) {
        int i = userRepo.deleteUserById(id);
        return i;
    }
    /**
     * 为确保缓存正确，会引入一些不必要的查询。。。
     */
    @Override
    @CacheEvict(cacheNames = "user",key = "#result")
    public int deleteUserByPhone(String phone) {
        User user = userRepo.retrieveUserByPhone(phone);
        int i = userRepo.deleteUserByPhone(phone);
        return user.getId();
    }
}
