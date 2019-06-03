package com.ssmr.c09;

import com.ssmr.c09.beanpostprocessor.BeanService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class C09Test {
    /*
    * spring运行时报log4j的实现找不到
    * 1. 添加VM options: -Dlog4j.configuration=file:绝对路径/log4j.properties
    * 2. 在web.xml中添加
    * */
    @Test
    public void test0(){
        //初始化SpringIoC容器
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("com/ssmr/c09/spring-configc09.xml");
        JuiceMaker2 juiceMaker = (JuiceMaker2) context.getBean("juiceMaker");
        System.out.println(juiceMaker.makeJuice());
        context.close();
        /*
        * 打印结果
    Creating shared instance of singleton bean 'beanPostProcessor'
    Creating shared instance of singleton bean 'disposableBean'
        BeanPostProcessImpl初始化前,传入的Bean类型 DisposableBeanImpl ,名称 disposableBean。
        BeanPostProcessImpl初始化后,传入的Bean类型 DisposableBeanImpl,名称 disposableBean。
    Creating shared instance of singleton bean 'juiceMaker'
        BeanPostProcessImpl初始化前,传入的Bean类型 JuiceSource ,名称 juiceSource。
        BeanPostProcessImpl初始化后,传入的Bean类型 JuiceSource,名称 juiceSource。
            JuiceMaker2 调用到接口 BeanNameAware 的 setBeanName 方法
            JuiceMaker2 调用到接口 BeanFactoryAware 的 setBeanFactory 方法
            JuiceMaker2 调用到接口 ApplicationContextAware 的 setApplicationContext 方法
        BeanPostProcessImpl初始化前,传入的Bean类型 JuiceMaker2 ,名称 juiceMaker。
            JuiceMaker2 调用到接口 InitializingBean 的 afterPropertiesSet 方法
            JuiceMaker2中的自定义初始化方法。
        BeanPostProcessImpl初始化后,传入的Bean类型 JuiceMaker2,名称 juiceMaker。
            制作果汁：JuiceSource(name=abc, sugar=true, size=12)
    Closing org.springframework.context.support.ClassPathXmlApplicationContext@4c203ea1, started on Sat May 25 16:36:36 CST 2019
            JuiceMaker2中的自定义销毁方法。
        DisposableBean的destroy方法
        * */
    }

    @Test
    public void testMyBeanPostProcessor(){
        ClassPathXmlApplicationContext context =
          new ClassPathXmlApplicationContext("com/ssmr/c09/spring-configc09.xml");

        BeanService beanService = (BeanService) context.getBean("beanServiceImpl");
//        beanService.printHello(); 死循环
        context.close();
    }




}
