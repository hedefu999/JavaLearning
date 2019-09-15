package com.redpacket.service.impl;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.model.RedpacketUser;
import com.redpacket.repository.RedpacketRecordMapper;
import com.redpacket.repository.RedpacketUserMapper;
import com.redpacket.service.RedpacketUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("redpacketUserService")
public class RedpacketUserServiceImpl implements RedpacketUserService {
    private final Logger log = LoggerFactory.getLogger(RedpacketUserServiceImpl.class);

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
}
