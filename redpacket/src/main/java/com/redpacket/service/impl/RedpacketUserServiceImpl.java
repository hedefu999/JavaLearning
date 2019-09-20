package com.redpacket.service.impl;

import com.redpacket.FileUtils;
import com.redpacket.model.RedpacketRecord;
import com.redpacket.model.RedpacketUser;
import com.redpacket.repository.RedpacketRecordMapper;
import com.redpacket.repository.RedpacketUserMapper;
import com.redpacket.service.RedisRedpacketService;
import com.redpacket.service.RedpacketUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@Service("redpacketUserService")
public class RedpacketUserServiceImpl implements RedpacketUserService {
    private final Logger log = LoggerFactory.getLogger(RedpacketUserServiceImpl.class);
    private final String luaScriptFilePath = "lua/grab_red_packet.lua";

    @Autowired
    private RedpacketUserMapper redpacketUserMapper;
    @Autowired
    private RedpacketRecordMapper redpacketRecordMapper;

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    @Override
    public int grabRedpacket(Integer redpacketId, Integer userId) {
        RedpacketRecord redpacketRecord = redpacketRecordMapper.selectByPrimaryKey(redpacketId);
        //try {
        //    Thread.sleep(500);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
        //库存>0说明还有红包可以抢
        if (redpacketRecord.getStock() > 0){
            redpacketRecordMapper.decreaseRedpacket(redpacketId);
            return saveRedPacketUser(userId, redpacketRecord);
        }
        return 0;
    }

    int saveRedPacketUser(Integer userId, RedpacketRecord record){
        RedpacketUser redpacketUser = new RedpacketUser();
        redpacketUser.setRedpacketId(record.getId());
        redpacketUser.setUserId(userId);
        redpacketUser.setAmount(record.getUnitAmount());
        int i = redpacketUserMapper.grabRedpacket(redpacketUser);
        return i;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public int grabRedpacketWithVersion(Integer redpacketId, Integer userId) {
        RedpacketRecord record = redpacketRecordMapper.selectByPrimaryKey(redpacketId);
        if (record.getStock() > 0){
            int i = redpacketRecordMapper.decreaseRedpacketWithVersion(redpacketId,record.getVersion());
            if (i == 0){
                log.info("版本号不一致，未能操作");
                return 0;
            }
            return saveRedPacketUser(userId,record);
        }
        return 0;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public int retryGrabByDuration(Integer redpacketId, Integer userId) {
        long start = System.currentTimeMillis();
        //循环重试机制
        while (true){
            long end = System.currentTimeMillis();
            if (end - start > 100){//超过100毫秒即退出，不再重试
                return 0;
            }
            RedpacketRecord record = redpacketRecordMapper.selectByPrimaryKey(redpacketId);
            if (record.getStock() > 0){
                int result = redpacketRecordMapper.decreaseRedpacketWithVersion(redpacketId,record.getVersion());
                if (result == 0){
                    log.info("用户{}开始重试抢红包",userId);
                    continue;//失败重试
                }
                int i = saveRedPacketUser(userId, record);
                return i>0?1:2;
            }else {
                log.info("红包瓜分完毕");
                return 0;
            }
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public int retryGrabByTimes(Integer redpacketId, Integer userId) {
        for (int i = 0; i < 3; i++) {
            RedpacketRecord record = redpacketRecordMapper.selectByPrimaryKey(redpacketId);
            if (record.getStock() > 0){
                int result = redpacketRecordMapper.decreaseRedpacketWithVersion(redpacketId, record.getVersion());
                if (result == 0){
                    //log.info("用户{}开始重试抢红包",userId);
                    continue;
                }
                int saveResult = saveRedPacketUser(userId, record);
                return saveResult>0?1:2;
            }else {
                log.info("红包瓜分完毕");
                return 0;
            }
        }
        //log.info("重试次数耗尽");
        return 0;
    }

    /*-=-=-=-=-=- 使用redis服务 =-=-=-=-=-*/
    /**
     * 此处实现的redis抢红包逻辑
     * 全过程只有一次使用数据库操作，并且还是在新线程中操作，可以不影响主体流程的响应速度
     */
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisRedpacketService redisRedpacketService;
    private String luaScript;
    private static String luaScriptSHA;
    //private AtomicReference<String> luaScriptSHAAtomic = new AtomicReference<>();原子跟排他锁是两个场景下的策略

    /**
     * 红包的扣减和记录的生活是在redis中进行的
     */
    @Override
    public long grabRedpacketByRedis(Integer redpacketId, Integer userId) {
        luaScript = FileUtils.readClassPathFileToString(luaScriptFilePath);
        //设置redis中链表存储的内容是args，以连字符连接的userId和时间戳
        String args = userId+"-"+System.currentTimeMillis();
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        //存在并发问题，这里会多次打印，加锁就好了
        synchronized (this){
            if (StringUtils.isEmpty(luaScriptSHA)){
                luaScriptSHA = jedis.scriptLoad(luaScript);
                log.info("加载lua脚本拿到SHA = {}", luaScriptSHA);
            }
        }
        Long result = (Long) jedis.evalsha(luaScriptSHA, 1, redpacketId + "", args);
        if (result == 2){//返回2表示抢到的是最后一个红包，此时应将redis中的数据持久化
            log.info("红包抢完发生在{}",System.currentTimeMillis());
            String unitAmountStr = jedis.hget("red_packet_"+redpacketId,"unit_amount");
            BigDecimal unitAmount = new BigDecimal(unitAmountStr);
            log.info("当前线程名称：{}", Thread.currentThread().getName());
            long start = System.currentTimeMillis();
            /**
             * 新开线程保存数据，但此处写成了同步等待的方式，返回为空可以更节约时间
             */
            Future<Integer> asyncResult = redisRedpacketService.asyncSaveRedpacketResult(redpacketId, unitAmount);
            Integer saveCount = 0;
            try {
                saveCount = asyncResult.get(4, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.info("获取异步结果发生错误");
            }
            log.info("异步返回结果: {}, 耗时: {}", saveCount, System.currentTimeMillis()-start);
        }
        if (jedis != null && jedis.isConnected()){
            jedis.close();
        }
        return result;
    }
}
