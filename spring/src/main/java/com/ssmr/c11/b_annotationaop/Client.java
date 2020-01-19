package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {
  private final Logger log = LoggerFactory.getLogger(Client.class);
  private static RoleService roleService = null;
  private static UserService userService = null;
  @Before
  public void before(){
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AOPConfig.class);
      roleService = context.getBean(RoleService.class);
      userService = context.getBean(UserService.class);
  }

  public static void main(String[] args) {
    //showAdiceExecuteOrder();
    //executeAdiceWithException();
    testRoleVerifier2Service();
  }
  //执行顺序比较奇怪
  //@Around→@Before→@Around→@After→@AfterReturning
  public static void showAdiceExecuteOrder() {
    Role role = new Role();role.setId(13);role.setName("lucy");
    roleService.printRole(role);
    /**
     * around 环绕通知执行前
     * before 入参：roleName = lucy.
     * 13lucy
     * around 环绕通知执行后
     * after ... Role(id=13, name=lucy)
     * after returning ...
     */
  }
  //测试SpringAOP处理异常发生时
  //异常发生并且抛出阻断程序时的aop顺序：@around->@before->@after->after-throwing->异常栈信息
  //异常发生并捕获时（与正常的情况下顺序相同）： @around->@before->method->@around->@after->@after-returning
  public static void executeAdiceWithException(){
    Role role = new Role();role.setId(13);role.setName("lucy");
    roleService.printRole2(role);
    System.out.println("_._._._.引入异常_._._._.");
    roleService.printRole2(null);
  }

  public static void testRoleVerifier2Service() {
    //配置过@DeclareParents后可以进行接口强转以实现接口增强
    RoleVerifier roleVerifier = (RoleVerifier) roleService;
    Role role = new Role();role.setId(13);role.setName("lucy");
    if (roleVerifier.verify(role)){
      roleService.printRole3(role,27483);
    }
    /**
     * role实例不为空，可以打印
     * before 入参：roleName = lucy.
     * 入参role是：Role(id=13, name=lucy)，code = 27483
     * after ... Role(id=13, name=lucy)
     * after returning ...
     */
  }

  @Test
  public void test57(){
      userService.getRoleParseResult();
  }

}
