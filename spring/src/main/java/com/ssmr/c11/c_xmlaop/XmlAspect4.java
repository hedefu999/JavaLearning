package com.ssmr.c11.c_xmlaop;

import com.ssmr.c11.Role;
import org.aspectj.lang.ProceedingJoinPoint;

public class XmlAspect4 {
    public void cycleAdice(ProceedingJoinPoint joinPoint, Role role){
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
