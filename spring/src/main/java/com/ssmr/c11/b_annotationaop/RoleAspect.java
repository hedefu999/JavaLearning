package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * 定义切面（拦截器）
 */
@Aspect
public class RoleAspect {
  /**
   * 关于通知Adice的注解说明
   * execution表示执行方法时触发；*表示任意返回类型；全限定的类名和方法名；(..)表示任意参数
   * args用于向通知传递参数，通常参数列表与切点的参数列表一致,否则正则匹配不到就不会被织入
   * 其他通知的参数传递方式与此类似
   */
  @Before("execution(* com.ssmr.c11.b_annotationaop.RoleServiceImpl.printRole(..)) && args(role)")
  public void before(Role role){
    System.out.printf("before 入参：roleName = %s.\n",role.getName());
  }
  @Before("execution(* com.ssmr.c11.b_annotationaop.RoleServiceImpl.printRole(..)) && args(role,note)")
  public void before(Role role, String note){
    System.out.printf("before 入参：roleName = %s, note = %s.\n",role.getName(),note);
  }

  //>>>> 在within指定的包内寻找printRole方法
  //@After("execution(* com.ssmr.*.printRole(..)) && within(com.ssmr.c11.*)")


  //>>>> 复用切入点方法
  @Pointcut("execution(* com.ssmr.c11.b_annotationaop.RoleServiceImpl.printRole(..)) && args(role)")
  public void roleArgPointCut(Role role){}
  //再次定义一个用于重用方法配置的切点
  @Pointcut("execution(* com.ssmr.c11.b_annotationaop.RoleService.printRole(..))")
  public void anyArgsPointCut(){}


  //这里直接配置成上面方法的名字是为了重用method4Reusing()方法上面的切点配置
  //@After("roleArgPointCut()") 相当于 @After("execution(* com.ssmr.c11.b_annotationaop.RoleServiceImpl.printRole(..))")
  @After("roleArgPointCut(role)")
  public void afterMethod(Role role){
    System.out.println("after ... "+role);
  }
  @AfterThrowing("roleArgPointCut(role)")
  public void afterException(Role role){
    System.out.println("exception happended ...");
  }
  @AfterReturning("anyArgsPointCut()")
  public void afterReturing(){
    System.out.println("after returning ...");
  }


  /**
   * 环绕通知是SpringAOP中最强大的通知，Spring提供的ProceedingJoinPoint参数可以反射切点方法
   * @param joinPoint
   */
  @Around("anyArgsPointCut()")
  public void aroundAdice(ProceedingJoinPoint joinPoint){
    System.out.println("around 环绕通知执行前");
    try {
      joinPoint.proceed();
    }catch (Throwable e){
      System.err.println(e.getMessage());
    }
    System.out.println("around 环绕通知执行后");
  }
}
