package com.allkindsproxy.cglibproxy;

import org.springframework.cglib.proxy.Enhancer;

public class Client {
    public static void main(String[] args) {
        AgencyOperator operator = new AgencyOperator();

        Enhancer enhancer = new Enhancer();
        //设置增强类型
        enhancer.setSuperclass(Welcom.class);
        //定义代理逻辑对象为当前对象，代理逻辑对象必须实现MethodInterceptor接口
        enhancer.setCallback(operator);
        //生成代理对象
        Welcom agency = (Welcom) enhancer.create();

        agency.sayHello();
    }
}
