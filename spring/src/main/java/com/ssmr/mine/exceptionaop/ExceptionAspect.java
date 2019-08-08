package com.ssmr.mine.exceptionaop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
public class ExceptionAspect {

    private Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

    @Pointcut("execution(* com.ssmr.mine.exceptionaop.ITest.process(..))")
    public void catchPoint(){}

    //@Pointcut("@annotation(com.souche.worknumclient.common.annotation.ExceptionAnnotation)")
    //public void catchException(){}


    @AfterThrowing(value = "catchPoint()", throwing = "exception")
    public void afterThrowing(JoinPoint point, Throwable exception) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("方法：%s\n",point.toString()));
        builder.append(String.format("参数：%s\n",JSON.toJSONString(point.getArgs())));
        StackTraceElement[] stackTrace = exception.getStackTrace();
        builder.append(String.format("异常信息：%s - %s\n%s\n",exception.getMessage(),stackTrace[0].toString(),stackTrace[1].toString()));
        System.out.println(builder.toString());
    }

}
