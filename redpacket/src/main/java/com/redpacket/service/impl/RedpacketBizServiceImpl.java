package com.redpacket.service.impl;

import com.redpacket.model.RedpacketUser;
import com.redpacket.repository.RedpacketRecordMapper;
import com.redpacket.repository.RedpacketUserMapper;
import com.redpacket.service.RedpacketBizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service("redpacketBizService")
public class RedpacketBizServiceImpl implements RedpacketBizService {
    private final Logger log = LoggerFactory.getLogger(RedpacketBizServiceImpl.class);

    @Autowired
    private RedpacketUserMapper redpacketUserMapper;
    @Autowired
    private RedpacketRecordMapper redpacketRecordMapper;

    @Transactional
    @Override
    public int saveRedpacketUsers(Integer redpacketId, BigDecimal unitAmount, List<String> userIdTimestampList){
        log.info("新开线程保存数据：redpacketId = {}, unitAmount = {}, userIdTimestampList = {}", redpacketId,unitAmount,userIdTimestampList.size());
        log.info("当前线程名称：{}", Thread.currentThread().getName());
        if (CollectionUtils.isEmpty(userIdTimestampList)){
            log.info("集合为空，返回");
            return 0;
        }
        int saveCount = 0;
        for (String userIdTimeStamp : userIdTimestampList){
            //数据格式的设置见RedpacketUserServiceImpl
            String[] arr = userIdTimeStamp.split("-");
            String userIdStr = arr[0];
            String timeStr = arr[1];
            Integer userId = Integer.valueOf(userIdStr);
            long time = Long.parseLong(timeStr);
            RedpacketUser redpacketUser = new RedpacketUser();
            redpacketUser.setRedpacketId(redpacketId);
            redpacketUser.setUserId(userId);
            redpacketUser.setAmount(unitAmount);
            redpacketUser.setGrabTime(new Timestamp(time));
            saveCount+=redpacketUserMapper.insert(redpacketUser);
            redpacketRecordMapper.decreaseRedpacket(redpacketId);
        }
        return saveCount;
    }
}
