package com.test;

import com.redpacket.config.RootConfig;
import com.redpacket.config.WebAppInitializer;
import com.redpacket.config.WebConfig;
import com.redpacket.service.RedpacketUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@ComponentScan(
        value = "com.redpacket.*",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,value = {Controller.class, RestController.class}),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = {WebAppInitializer.class, WebConfig.class})
        })
public class SpringAsycTester {
    private final Logger log = LoggerFactory.getLogger(SpringAsycTester.class);

    @Autowired
    private RedpacketUserService redpacketUserService;

    @Test
    public void test(){
        log.info("当前线程名称：{}", Thread.currentThread().getName());
    }
}
