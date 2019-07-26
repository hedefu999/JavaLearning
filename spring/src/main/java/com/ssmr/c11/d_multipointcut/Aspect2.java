package com.ssmr.c11.d_multipointcut;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1)
public class Aspect2 {
    @Pointcut("execution(* com.ssmr.c11.d_multipointcut.MultiAspects.testMultiAspects(..))")
    public void pointcut(){}
    @Before("pointcut()")
    public void before(){
        System.out.println("before 2 ...");
    }
    @After("pointcut()")
    public void after(){
        System.out.println("after 2 ...");
    }
}
