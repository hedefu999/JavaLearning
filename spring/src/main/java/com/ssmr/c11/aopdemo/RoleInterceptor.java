package com.ssmr.c11.aopdemo;
//SpringAOP - Aspect
public class RoleInterceptor implements Interceptor {
  //SpringAOP - before Adice
  @Override
  public void before(Object obj) {
    System.out.println("before:"+obj.getClass().getSimpleName());
  }
  //SpringAOP - after Adice
  @Override
  public void after(Object obj) {
    System.out.println("after:"+obj.getClass().getSimpleName());
  }
  //SpringAOP - afterReturning Adice
  @Override
  public void afterReturning(Object obj) {
    System.out.println("正常执行Bean方法 after returning:"+obj.getClass().getSimpleName());
  }
  //SpringAOP - afterThrowing Adice
  @Override
  public void afterThrowing(Object obj) {
    System.out.println("发生异常 after throwing:"+obj.getClass().getSimpleName());
  }
}
