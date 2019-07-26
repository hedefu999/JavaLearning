package com.ssmr.c11.b_annotationaop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 定义切面（拦截器）
 */
@Aspect
public class RoleAspect2 {

  @Pointcut("execution(* com.ssmr.c11.b_annotationaop.RoleService.printRole2(..))")
  public void anyArgsPointCut(){}

  @Before("anyArgsPointCut()")
  public void before(){
    System.out.println("before ...");
  }

  @After("anyArgsPointCut()")
  public void afterMethod(){
    System.out.println("after ... ");
  }

  @AfterThrowing("anyArgsPointCut()")
  public void afterException(){
    System.out.println("exception happended ...");
  }

  @AfterReturning("anyArgsPointCut()")
  public void afterReturing(){
    System.out.println("after returning ...");
  }

  /**
   * around 环绕通知执行前
   * before ...
   * 编号：13，名字：lucy。around 环绕通知执行后
   * after ...
   * after returning ...
   * _._._._.引入异常_._._._.
   * around 环绕通知执行前
   * before ...
   * null
   * com.ssmr.c11.b_annotationaop.RoleServiceImpl.printRole2(RoleServiceImpl.java:15)
   * around 环绕通知执行后
   * after ...
   * after returning ...
   */
  //环绕通知会阻止afterThrowing 方法的执行，但如果e.getCause().getMessage()
  @Around("anyArgsPointCut()")
  public void aroundAdice(ProceedingJoinPoint joinPoint){
    System.out.println("around 环绕通知执行前");
    try {
      joinPoint.proceed();
    }catch (Throwable e){
      /**
       * - - - - 1.抛出异常，许多通知将不能执行,但afterThrowing会被执行
       控制台输出：
       * around 环绕通知执行前
       * before ...
       * after ...
       * exception happended ...
       * Exception in thread "main" java.lang.NullPointerException
       */
      //System.out.println(e.getCause().getMessage());
      /**
       * - - - - 2. 不执行AfterThrowing
       * 异常被捕获并且打印出友好的信息，程序可以继续执行，但AfterThrowing Adice不会被执行
       控制台输出：
       * around 环绕通知执行前
       * before ...
       * null
       * com.ssmr.c11.b_annotationaop.RoleServiceImpl.printRole2(RoleServiceImpl.java:15)
       * around 环绕通知执行后
       * after ...
       * after returning ...
       */
      System.err.println(e.getMessage());
      System.err.println(e.getStackTrace()[0].toString());
    }
    System.out.println("around 环绕通知执行后");
  }

}
