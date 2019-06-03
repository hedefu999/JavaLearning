package com.ssmr.c09.beanpostprocessor;

import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@Data
public class MyProxy implements InvocationHandler {
  private String name;
  private Object object;
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return method.invoke(proxy,args);
  }

}
