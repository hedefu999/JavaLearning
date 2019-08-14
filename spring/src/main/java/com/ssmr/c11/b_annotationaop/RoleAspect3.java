package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;
import org.aspectj.lang.annotation.*;

/**
 * 定义切面（拦截器）
 */
@Aspect
public class RoleAspect3 {
  /**
   * 注解 @DeclareParents value的含义是对RoleService接口进行增强，引入新的方法
   * defaultImpl表示引入的增强的默认实现类
   * “+” 号，表示只要是自身及其子类都可以添加新的方法,这里将RoleServiceImpl作为了父类
   * ref to https://www.cnblogs.com/xxdsan/p/6496332.html
   */
  @DeclareParents(value = "com.ssmr.c11.b_annotationaop.RoleServiceImpl+", defaultImpl = RoleVerifierImpl.class)
  public RoleVerifier roleVerifier;


  @Pointcut("execution(* com.ssmr.c11.b_annotationaop.RoleServiceImpl.printRole3(..)) && args(role,code)")
  public void roleCodeArgsPointCut(Role role, Integer code){}

  @Before("roleCodeArgsPointCut(role,code)")
  public void before(Role role,Integer code){
    System.out.printf("before 入参：roleName = %s.\n",role.getName());
  }
  @After("roleCodeArgsPointCut(role,code)")
  public void afterMethod(Role role,Integer code){
    System.out.println("after ... "+role);
  }
  @AfterThrowing("roleCodeArgsPointCut(role,code)")
  public void afterException(Role role,Integer code){
    System.out.println("after throwing ...");
  }
  @AfterReturning("roleCodeArgsPointCut(role,code)")
  public void afterReturing(Role role,Integer code){
    System.out.println("after returning ...");
  }

}
