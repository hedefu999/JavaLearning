package com.ssmr.c11.a_apiaop;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class JavaAPIAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("after returning"+method.getName());
    }

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("before advice"+method.getName());
    }
}
