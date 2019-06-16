package com.ssmr.c11.springaopdemo;

import com.ssmr.c11.Role;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 定义切面（拦截器）
 */
@Aspect
public class RoleAspect {
  /**
   * 注解 @DeclareParents value的含义是对RoleServiceImpl类进行增强，引入新的接口
   * defaultImpl表示引入的接口的默认实现类
   */
  @DeclareParents(value = "com.ssmr.c11.springaopdemo.RoleServiceImpl+",
    defaultImpl = RoleVerifierImpl.class)
  public RoleVerifier roleVerifier;
  /**
   * 关于通知Adice的注解说明
   * execution表示执行方法时触发；*表示任意返回类型；全限定的类名和方法名；(..)表示任意参数
   * args用于向通知传递参数，通常参数列表与切点的参数列表一致,否则正则匹配不到就不会被织入
   * 其他通知的参数传递方式与此类似
   */
  @Before("execution(* com.ssmr.c11.springaopdemo.RoleServiceImpl.printRole(..)) && args(role)")
  public void before(Role role){
    System.out.printf("前置通知接收到参数：roleName = %s.\n",role.getName());
  }
  @Before("execution(* com.ssmr.c11.springaopdemo.RoleServiceImpl.printRole(..)) && args(role,note)")
  public void before(Role role, String note){
    System.out.printf("前置通知接收到参数：roleName = %s, note = %s.\n",role.getName(),note);
  }
  /**
   * 关于AspectJ指示器的示例
   */
  //>>>> 在within指定的包内寻找printRole方法
  //@After("execution(* com.ssmr.*.printRole(..)) && within(com.ssmr.c11.*)")
  //>>>> 复用切入点方法
  @Pointcut("execution(* com.ssmr.c11.springaopdemo.RoleServiceImpl.printRole(..))")
  public void method4Reusing(){}
  @After("method4Reusing()")
  public void afterMethod(){
    System.out.println("after ... ");
  }
  @AfterReturning("execution(* com.ssmr.c11.springaopdemo.RoleServiceImpl.printRole(..))")
  public void afterReturing(){
    System.out.println("no exception exist ...");
  }
  @AfterThrowing("method4Reusing()")
  public void afterException(){
    System.out.println("exception happended ...");
  }

  /**
   * 环绕通知是SpringAOP中最强大的通知，Spring提供的ProceedingJoinPoint参数可以反射切点方法
   * @param joinPoint
   */
  @Around("method4Reusing()")
  public void aroundAdice(ProceedingJoinPoint joinPoint){
    System.out.println("环绕通知执行前");
    try {
      joinPoint.proceed();
    }catch (Throwable e){
      System.err.println(e.getMessage());
    }
    System.out.println("环绕通知执行后");

  }
}
