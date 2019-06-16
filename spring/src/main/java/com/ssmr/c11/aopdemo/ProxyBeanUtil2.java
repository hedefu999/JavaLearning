package com.ssmr.c11.aopdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyBeanUtil2 implements InvocationHandler{
  private Object obj;
  private Interceptor interceptor;

  public static Object getBean(Object obj, Interceptor interceptor){
    //这种写法每次要创建一个ProxyBeanUtil2实例
    ProxyBeanUtil2 _this = new ProxyBeanUtil2();
    _this.interceptor = interceptor;
    _this.obj = obj;

    Object proxy = Proxy.newProxyInstance(
      obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),_this
    );
    return proxy;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args){
    boolean exceptionFlag = false;
    //此处不可以使用proxy去执行，会造成死循环
    interceptor.before(obj);
    try {
      System.out.print("执行bean方法: ");
      Object retObj = method.invoke(obj,args);//不可method.invoke(proxy,args);
      System.out.println(retObj == obj);//false
//      System.out.println(retObj.hashCode());//retObj为null ！
    }catch (Exception e){
      exceptionFlag = true;
      System.err.println("异常原因："+e.getMessage());
    }finally {
      interceptor.after(obj);
    }

    //使用异常标志，代码更利索
    if (exceptionFlag){
      interceptor.afterThrowing(obj);
    }else {
      interceptor.afterReturning(obj);
    }
    return obj;
  }
}