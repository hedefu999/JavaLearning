package com.ssmr.c11.c_xmlaop;

import com.ssmr.c11.Role;
import org.aspectj.lang.ProceedingJoinPoint;

public class XmlAspect {
    public void before(){
        System.out.println("before ...");
    }
    public void before2(Role role){
        System.out.printf("before2... role.name=%s.",role.getName());
    }
    public void after(){
        System.out.println("after ...");
    }
    public void afterThrowing(){
        System.out.println("after-throwing ...");
    }
    public void afterReturning(){
        System.out.println("after-returning ...");
    }
    public void around(ProceedingJoinPoint joinPoint){
        System.out.println("around 环绕通知执行前");
        try {
            joinPoint.proceed();
        }catch (Throwable e){
            System.err.println(e.getMessage());
        }
        System.out.println("around 环绕通知执行后");
    }
}
