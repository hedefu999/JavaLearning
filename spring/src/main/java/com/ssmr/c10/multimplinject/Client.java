package com.ssmr.c10.multimplinject;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("com/ssmr/c10/multi-interface-impl.xml");
        context.refresh();
        IPayTool alipayTool = context.getBean(AlipayTool.class);
        alipayTool.pay();
    }
}
