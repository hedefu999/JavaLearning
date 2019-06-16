package com.ssmr.c11.aopdemo;

public interface Interceptor {
  void before(Object obj);
  void after(Object obj);
  void afterReturning(Object obj);
  void afterThrowing(Object obj);
}
