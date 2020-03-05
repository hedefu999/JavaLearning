package com.redis._08springredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userBasicService")
public class UserBasicServiceImpl implements UserBasicService {
    private final Logger log = LoggerFactory.getLogger(UserBasicServiceImpl.class);
    @Autowired
    private UserRepo userRepo;

    //mybatis回填生成的主键到了user，但不能直接返回主键，否则加入缓存的就是一个数字，在selectbyid会莫名出错
    //mybatis回填主键的方式是设置insert的属性  useGeneratedKeys="true" keyProperty="id"
    //使用spEL从返回结果中取字段，执行该方法后出现key为 " user::18 "的缓存记录
    @Override
    @CachePut(value = "user",key = "#result.id")
    public User saveUser(User user) {
        int i = userRepo.createUser(user);
        log.info("SQL执行结果：受影响的行数 = {}",i);
        log.info("mybatis将生成的主键设置到了user中：{}", user.getId());
        return user;
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

    /**
     * 缓存有这样一个坑：
     * 如果insert操作返回了主键，但错误地将它放入了缓存，这样缓存中存在user::key
     * 此时使用select id，框架会认为user::key就是应当返回的缓存，而select返回的是user，这样总是映射错误
     * 缓存可能会搞坏原本正常的方法逻辑，，，
     */
    @Override
    @Cacheable(cacheNames = "user", key = "#id")
    public User getUserById(Integer id) {
        log.info("依据主键id={}查询用户");
        User user = userRepo.retrieveUserById(id);
        return user;
    }

    /**
     * - @Cacheable无法通过#result.id获得主键，无法添加到缓存
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
     * beforeInvocation = true  在方法执行前删除key防止有线程从缓存取到数据。默认false
     */
    @Override
    @CacheEvict(cacheNames = "user",key = "#id",beforeInvocation = true)
    public int deleteUserById(Integer id) {
        int i = userRepo.deleteUserById(id);
        return i;
    }
    /**
     * 为确保缓存正确，会引入一些不必要的查询
     * allEntries=true并不表示清空redis缓存，只是清空所有key以user::开头的缓存，不论phone筛选条件是否命中了缓存中的记录
     * 一删百删，要求删除缓存中的所有匹配当前命名规则(以cacheNames:: 开头)的所有key
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
        int result = 0;
        for (User user : userList) {
            int saveUser = saveUser(user).getId();
            result += saveUser;
        }
        return result;
    }
}
