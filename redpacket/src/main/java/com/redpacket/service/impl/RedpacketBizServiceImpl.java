package com.redpacket.service.impl;

import com.redpacket.model.RedpacketRecord;
import com.redpacket.service.RedpacketBasicService;
import com.redpacket.service.RedpacketBizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service("redpacketBizService")
public class RedpacketBizServiceImpl implements RedpacketBizService {
    private final Logger log = LoggerFactory.getLogger(RedpacketBizServiceImpl.class);
    @Autowired
    private RedpacketBasicService redpacketBasicService;


    @Override
    public void testVoidAsyncImpact() {
        long start = System.currentTimeMillis();
        redpacketBasicService.doTimeConsumedOperation();
        long end = System.currentTimeMillis();
        log.info("外层方法执行线程名：{} start = {},end = {}, 总耗时 = {}",Thread.currentThread().getName(),start,end);
    }

    @Override
    public void testNonVoidAsyncImpact(){
        long start = System.currentTimeMillis();
        Future<String> futureResult = redpacketBasicService.doHasFeedBackOperation();
        long end = System.currentTimeMillis();
        log.info("外层方法执行线程名：{} 开始时间 = {},调用完成时间点：{}, 总耗时 = {}",Thread.currentThread().getName(),start,end);
        String result = null;
        try {
            result = futureResult.get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        log.info("最终返回结果：{}, 执行完成时间点：{}，总耗时：{}", result, end, end-start);
    }

    @Transactional
    @Override
    public void testAsyncTxImpact(){
        long start = System.currentTimeMillis();
        RedpacketRecord record = new RedpacketRecord();
        record.setUserId(346732);
        record.setAmount(new BigDecimal("240.00"));
        record.setSendDate(new Date());
        record.setTotal(12);
        record.setUnitAmount(new BigDecimal("20.00"));
        record.setStock(12);
        record.setNote("发了12个红包");

        int result = redpacketBasicService.saveRedpacketRecord(record);
        redpacketBasicService.doTimeConsumedOperation();

        long end = System.currentTimeMillis();
        log.info("外层方法执行线程名：{} start = {},end = {}, 总耗时 = {}",Thread.currentThread().getName(),start,end);
    }
}
