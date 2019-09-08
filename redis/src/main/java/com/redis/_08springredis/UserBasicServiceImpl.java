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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userBasicService")
public class UserBasicServiceImpl implements UserBasicService {
    private final Logger log = LoggerFactory.getLogger(UserBasicServiceImpl.class);
    @Autowired
    private UserRepo userRepo;

    @Override
    //此处返回的role中id是数据库生成的无法填充,建议使用其他唯一索引字段.或者在get方法上设置key为主键的缓存
    @CachePut(value = "user",key = "#user.phone")
    public int saveUser(User user) {
        int i = userRepo.createUser(user);
        return i;
    }

    @Override
    @CachePut(cacheNames = "user",key = "#user.id")
    public User updateUserById(User user) {
        int i = userRepo.updateUserById(user);
        return user;
    }

    @Override
    //使用主键做缓存的key，但若使用唯一索引更新记录，就要返回查到的记录的主键，否则就不必做缓存更新了
    @CachePut(cacheNames = "user",key = "#result.id")
    public User updateUserByPhone(User user) {
        int i = userRepo.updateUserByPhone(user);
        //确保id不为空，这会因为缓存增加不必要的逻辑
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
     * - @Cacheablew无法通过#result.id获得主键，无法添加到缓存
     * 所以使用@CachePut从返回结果中取id
     * 没有使用主键查询，每次getByPhone时都会从数据库查询，方法并没有因为缓存而得到性能提升
     */
    @Override
    @CachePut(cacheNames = "user",key = "#result.id")
    public User getUserByPhone(String phone) {
        User user = userRepo.retrieveUserByPhone(phone);
        log.info("查询到用户：{}",user);
        return user;
    }

    /**
     * 为了使用#result.id,在get方法上使用@CachePut注解
     * 但@CachePut是用于更新方法的，不会依据key从缓存中查找
     * 不论user里id有没有设置，框架都不会使用入参的id生成key去查缓存，而是直接查数据库
     * 但如果user的id始终有值，@Cacheable就能从缓存里查找，这样入参就很不符合常规
     * 框架不能判断user的那个属性是主键
     * - @Cacheable 与 @CachePut的功能不同，不可混用
     */
    @Override
    @CachePut(cacheNames = "user",key = "#result.id")
    //@Cacheable(cacheNames = "user",key="#user.id")
    public User getUserByPhone(User user){
        User user1 = userRepo.retrieveUserByPhone(user.getPhone());
        return user1;
    }

    /**
     * 使用缓存时需要注意返回的int究竟是主键还是操作的行数
     */
    @Override
    @CacheEvict(cacheNames = "user",key = "#id",beforeInvocation = true)
    public int deleteUserById(Integer id) {
        int i = userRepo.deleteUserById(id);
        return i;
    }
    /**
     * 为确保缓存正确，会引入一些不必要的查询
     * allEntries=true并不表示清空redis缓存，只是清空所有key以user::开头的缓存
     */
    @Override
    @CacheEvict(cacheNames = "user",key = "#result",allEntries = true)
    public int deleteUserByPhone(String phone) {
        User user = userRepo.retrieveUserByPhone(phone);
        if (user == null){
            return 0;
        }
        int i = userRepo.deleteUserByPhone(phone);
        return user.getId();
    }

    /**
     * 调用同一个类内的方法存在自调用失效问题，由于无法通过代理执行，saveUser方法上的@CachePut注解无法生效
     * 没有任何缓存被设置到redis中
     * 上述情况与数据库事务的自调用失效问题一致
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int saveUsers(List<User> userList){
        int result = userList.stream().mapToInt(this::saveUser).sum();
        return result;
    }
}
