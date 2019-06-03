package com.ssmr.c09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 从打印出的日志可见，BeanPostProcessor针对所有bean，任何Bean的创建动作都会有接口内的两个方法执行
 * 对于声明多个bean的BeanPostProcessor接口方法调用顺序
 * A.before
 * B.before
 * ...
 * A.post
 * B.post
 */
public class BeanPostProcessorImpl implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.printf("BeanPostProcessImpl初始化前,传入的Bean类型 %s ,名称 %s。\n",bean.getClass().getSimpleName(),beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.printf("BeanPostProcessImpl初始化后,传入的Bean类型 %s,名称 %s。\n",bean.getClass().getSimpleName(),beanName);
        return bean;
    }
}
