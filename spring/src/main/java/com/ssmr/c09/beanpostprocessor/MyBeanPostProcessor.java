package com.ssmr.c09.beanpostprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//TODO 这个BeanPostProcessor如何进行bean方法调用监控
public class MyBeanPostProcessor implements BeanPostProcessor {
  private static final Logger log = LoggerFactory.getLogger("MyBeanPostProcessor");
  //用于存放被代理的Bean的名字和实际的代理,判断是否已经代理过
  private Map<String,Object> map = new ConcurrentHashMap(100);

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    MyProxy proxy = new MyProxy();
    if (beanName.contains("DB")||beanName.contains("Proxy")){
      //某些bean不进行代理
      return bean;
    }
    Object object = map.get(beanName);
    if (object != null){
      log.info("已经代理过");
      return object;
    }
    proxy.setName(beanName);
    proxy.setObject(object);
    Class[] interfaces = bean.getClass().getInterfaces();
    if (interfaces.length == 0){
      log.info("{}没有实现任何接口，不能代理",beanName);
      return bean;
    }
    Object realProxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(),interfaces,proxy);
    map.put(beanName,realProxy);
    log.info("返回{}的代理对象", bean.getClass().getSimpleName());
    return realProxy;
  }
}
