package com.ssmr.mine.exceptionaop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AopConfig.class)
public class ClientTest {
    @Autowired
    private ITest iTest;
    @Autowired
    private ExceptionAspect exceptionAspect;
    @Test
    public void test0(){
        iTest.process("hedefu");
    }
}
