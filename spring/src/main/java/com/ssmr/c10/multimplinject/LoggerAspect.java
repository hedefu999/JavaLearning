package com.ssmr.c10.multimplinject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggerAspect {
    @Around("execution(* com.ssmr.c10.multimplinject.AlipayTool.*(..))")
    public void aspectb(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("before method ---");
        joinPoint.proceed();
        System.out.println("after method ---");
    }
}
