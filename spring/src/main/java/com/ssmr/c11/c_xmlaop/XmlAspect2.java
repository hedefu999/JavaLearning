package com.ssmr.c11.c_xmlaop;

import com.ssmr.c11.Role;
import org.aspectj.lang.ProceedingJoinPoint;

public class XmlAspect2 {
    public void before(Role role){
        System.out.printf("before ...role.name=%s\n",role.getName());
    }
    public void after(Role role){
        System.out.printf("after ...role.name=%s\n",role.getName());
    }
    public void afterThrowing(Role role){
        System.out.printf("after-throwing ...role.name=%s\n",role.getName());
    }
    public void afterReturning(Role role){
        System.out.printf("after-returning ...role.name=%s\n",role.getName());
    }
    public void around(ProceedingJoinPoint joinPoint,Role role){
        System.out.printf("around 环绕通知执行前,role.name=%s\n",role.getName());
        try {
            joinPoint.proceed();
        }catch (Throwable e){
            System.err.printf(e.getMessage());
        }
        System.out.printf("around 环绕通知执行后,role.name=%s\n",role.getName());
    }
}
