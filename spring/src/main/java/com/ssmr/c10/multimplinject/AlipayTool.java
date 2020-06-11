package com.ssmr.c10.multimplinject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("alipayTool")
public class AlipayTool implements IPayTool {
    @Autowired
    @Qualifier("alipayTool")
    private AlipayTool self;

    @Override
    public void pay(){
        System.out.println(self.getClass().getSimpleName());
        System.out.println("执行自调用");
        self.BBB();
    }
    public void BBB(){
        System.out.println("B方法");
    }
}
