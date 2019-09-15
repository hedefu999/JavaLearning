package com.test;

import com.redpacket.config.RootConfig;
import com.redpacket.config.WebAppInitializer;
import com.redpacket.config.WebConfig;
import com.redpacket.model.RedpacketRecord;
import com.redpacket.repository.RedpacketRecordMapper;
import com.redpacket.repository.RedpacketUserMapper;
import com.redpacket.service.RedpacketUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RestController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
@ComponentScan(
        value = "com.redpacket.*",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,value = {Controller.class, RestController.class}),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = {WebAppInitializer.class, WebConfig.class})
        })
public class RedPacketMapperTester {
    @Autowired
    RedpacketUserService redpacketUserService;
    @Autowired
    private RedpacketUserMapper redpacketUserMapper;
    @Autowired
    private RedpacketRecordMapper redpacketRecordMapper;
    @Test
    public void testRecordMapper(){
        //redpacketUserService.grabRedpacket(1,1);
        RedpacketRecord redpacketRecord = redpacketRecordMapper.selectByPrimaryKey(1);
        System.out.println(redpacketRecord.getAmount());
    }
    @Test
    public void testUserMapper(){
        int insert = redpacketUserMapper.insert(null);
        System.out.println(insert);
    }
}
