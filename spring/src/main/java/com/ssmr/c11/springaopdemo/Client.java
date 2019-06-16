package com.ssmr.c11.springaopdemo;

import com.ssmr.c11.Role;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
  public static void main2(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AOPConfig.class);
    RoleService roleService = ctx.getBean(RoleService.class);
    Role role = new Role();role.setId(13);role.setName("lucy");
    roleService.printRole(role);
    System.out.println("\n测试SpringAOP处理异常发生时");
    roleService.printRole(null);
    System.out.println("运行完成");

    /**
     * 环绕通知可以捕获异常，让运行完成打印出来
     *
     * 环绕通知执行前
     * 前置通知接收到参数：roleName = lucy.
     * 13lucy
     * 环绕通知执行后
     * after ...
     * no exception exist ...
     *
     * 测试SpringAOP处理异常发生时
     * 环绕通知执行前
     * null
     * 环绕通知执行后
     * after ...
     * no exception exist ...
     * 运行完成
     */
  }

  public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AOPConfig.class);
    RoleService roleService = ctx.getBean(RoleService.class);
    RoleVerifier roleVerifier = (RoleVerifier) roleService;
    Role role = new Role();role.setId(13);role.setName("lucy");
    if (roleVerifier.verify(role)){
      roleService.printRole(role);
    }
    /**
     * role实例不为空，可以打印
     * 环绕通知执行前
     * 前置通知接收到参数：roleName = lucy.
     * 13lucy
     * 环绕通知执行后
     * after ...
     * no exception exist ...
     */
  }
}
