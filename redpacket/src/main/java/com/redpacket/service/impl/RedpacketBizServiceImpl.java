package com.redpacket.service.impl;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.service.RedpacketBasicService;
import com.redpacket.service.RedpacketBizService;
import com.redpacket.service.RedpacketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service("redpacketBizService")
public class RedpacketBizServiceImpl implements RedpacketBizService {
    private final Logger log = LoggerFactory.getLogger(RedpacketBizServiceImpl.class);
    private static RedpacketRecord record;
    static {
        record = new RedpacketRecord();
        record.setUserId(346732);
        record.setAmount(new BigDecimal("240.00"));
        record.setSendDate(new Date());
        record.setTotal(12);
        record.setUnitAmount(new BigDecimal("20.00"));
        record.setStock(12);
        record.setNote("发了12个红包");

    }
    @Autowired
    private RedpacketBasicService redpacketBasicService;
    @Autowired
    private RedpacketService redpacketService;


    @Override
    public void testVoidAsyncImpact() {
        long start = System.currentTimeMillis();
        redpacketBasicService.doTimeConsumedOperation();
        long end = System.currentTimeMillis();
        log.info("外层方法执行线程名：{} start = {},end = {}, 总耗时 = {}",Thread.currentThread().getName(),start,end,end-start);
    }

    @Override
    public void testNonVoidAsyncImpact(){
        long start = System.currentTimeMillis();
        Future<String> futureResult = redpacketBasicService.doHasFeedBackOperation();
        long end = System.currentTimeMillis();
        log.info("外层方法执行线程名：{} 开始时间 = {},调用完成时间点：{}, 总耗时 = {}",Thread.currentThread().getName(),start,end,end-start);
        String result = null;
        try {
            result = futureResult.get(2000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            end = System.currentTimeMillis();
            log.info("等待耗时方法执行，未能及时返回结果：{}，返回时间点：{},最终耗时：{}",e,end,end-start);
            return;
        }
        end = System.currentTimeMillis();
        log.info("最终返回结果：{}, 执行完成时间点：{}，总耗时：{}", result, end, end-start);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    @Override
    public void processRedpacketBizLogic(){
        String threadName = Thread.currentThread().getName();
        String txName = TransactionSynchronizationManager.getCurrentTransactionName();
        int affectedRows = redpacketBasicService.saveRedpacket(record);
        log.info("方法所处线程：{}，所处事务：{}, 返回的主键 = {},\n保存操作影响的行数：{}，将默认扣减一个红包作为平台分成！",threadName, txName, record.getId(), affectedRows);
        redpacketService.processCommonBizLogic(record.getId());
        //processCommonBizLogic(record.getId());
    }

    @Autowired
    private Executor taskExecutor;
    //重复的方法，用于测试自调用失效: @Async @Transactional都存在自调用失效的问题，但技术上都可以手写实现
    //@Async("taskExecutor")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void processCommonBizLogic(Integer redpacketId){
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                String txName = TransactionSynchronizationManager.getCurrentTransactionName();
                //手动新建线程后，decreaseRedpacket自调用时无法获取并新建事务（或许可以配置成REQUIRED_NEW），两层方法的事务管理都失败了，都没有回滚
                int i = redpacketBasicService.decreaseRedpacket(redpacketId);
                log.info("方法所处线程名：{}, 所处事务名：{}, 扣减红包操作数据库记录行数：{}",threadName, txName,i);
                throw new RuntimeException("很遗憾，国家规定平台不允许直接分成");
            }
        });

    }
}
