package com.redpacket.service.impl;

import com.redpacket.service.RedpacketBasicService;
import com.redpacket.service.RedpacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service("redpacketService")
public class RedpacketServiceImpl implements RedpacketService {
    private final Logger log = LoggerFactory.getLogger(RedpacketServiceImpl.class);
    @Autowired
    private RedpacketBasicService redpacketBasicService;

    //打开或关闭（1）(2)处的代码有不同效果
    @Async("taskExecutor") //(1)
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void processCommonBizLogic(Integer redpacketId){
        String txName = TransactionSynchronizationManager.getCurrentTransactionName();
        int i = redpacketBasicService.decreaseRedpacket(redpacketId);
        log.info("当前所处事务的名字：{}, 扣减红包操作数据库记录行数：{}",txName,i);
        throw new RuntimeException("很遗憾，国家规定平台不允许直接分成"); //(2)
    }

}
