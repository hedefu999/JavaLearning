package com.ssmr.c11.c_xmlaop;

import com.ssmr.c11.Role;
import org.aspectj.lang.ProceedingJoinPoint;

public class XmlAspect3 {
    public void around(ProceedingJoinPoint joinPoint,Role role,AnnoAop annoAop){
        //环绕通知操作入参，将入参的属性替换为切点上注解中填入的内容
        System.out.printf("around 环绕通知执行前,role.name=%s\n",role.getName());
        String neighborWang = annoAop.value();
        try {
            role.setName(neighborWang);
            joinPoint.proceed();
        }catch (Throwable e){
            System.err.printf("%s\n",e.getMessage());
        }
        System.out.printf("around 环绕通知执行后,role.name=%s\n",role.getName());
    }
}
