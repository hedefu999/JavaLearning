package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
public class RoleAspect4 {
    private final Logger log = LoggerFactory.getLogger(RoleAspect4.class);

    @Pointcut("execution(* com.ssmr.c11.b_annotationaop.RoleServiceImpl.processParam(..)) && args(role,addDesc)")
    public void parseParamPointcut(Role role, boolean addDesc){
        log.info("切面定义：role = {}, code = {}", role, addDesc);//这一行不会打出来。。。
    }

    //@Around("parseParamPointcut(com.ssmr.c11.Role, boolean) && args(role, addDesc)")
    //public String parseParamAround(ProceedingJoinPoint joinPoint,Role role, boolean addDesc){
    //    log.info("环绕通知前, role = {}, addDesc = {}", role, addDesc);
    //    Object proceed = null;
    //    try {
    //        proceed = joinPoint.proceed();
    //        log.info("被代理方法返回内容：proceed = {}", proceed);
    //        return proceed.toString();
    //    } catch (Throwable throwable) {
    //        log.info("发生异常", throwable);
    //    }finally {
    //        return proceed == null?"":proceed.toString();
    //    }
    //}

    @AfterReturning(pointcut = "parseParamPointcut(com.ssmr.c11.Role, boolean)", returning = "result")
    public void parseParamAfter(String result){
        log.info("切面位于的线程名：{}", Thread.currentThread().getName());
        log.info("切面位于的事务名：{}", TransactionSynchronizationManager.getCurrentTransactionName());
        //切面加太多会把原接口搞的很慢，切面对于事务的影响？
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("返回通知，返回结果：result = {}", result);
    }

}
