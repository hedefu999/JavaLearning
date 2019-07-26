package com.ssmr.c11.a_aopprimary;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyBeanUtil {
  public static Object getBean(Object obj, Interceptor interceptor){

    Object proxy = Proxy.newProxyInstance(
      obj.getClass().getClassLoader(),
      obj.getClass().getInterfaces(),
      new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
          interceptor.before(obj);
          try {
            System.out.print("执行bean方法: ");
            method.invoke(obj,args);
            interceptor.afterReturning(obj);
          }catch (Exception e){
            interceptor.afterThrowing(obj);
            System.err.println("异常原因："+e.getMessage());
          }finally {
            interceptor.after(obj);
          }
          return obj;
        }
    });
    //第三个参数使用lambda表达式：(proxy1, method, args) -> { ... }
    return proxy;
  }
}
