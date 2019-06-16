package com.ssmr.c10;

import com.ssmr.c10.annoInject.ComponentScanDef;
import com.ssmr.c10.annoInject.Role;
import com.ssmr.c10.annoInject.RoleDataService;
import com.ssmr.c10.xmlInject.UserRoleAssemble;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class C10Test {
  /*****10.4.1 使用@Component装配Bean********/
  @Test
  public void testComplexAssembleUsingNameSpace(){
    ApplicationContext context = new ClassPathXmlApplicationContext("com/ssmr/c10/spring-configc10.xml");
    UserRoleAssemble userRoleAssemble = (UserRoleAssemble) context.getBean("userRoleAssemble2");
    System.out.println(userRoleAssemble);
  }

  @Test
  public void testComponentScanDefClass(){
    ApplicationContext context = new AnnotationConfigApplicationContext(ComponentScanDef.class);
    Role role = (Role) context.getBean("role");
    Role role1 = context.getBean(Role.class);
    System.out.println(role.getNote()+role1.getNote());
    System.out.println(role == role1);//true
  }

  /*****10.5 装配的混合使用****/
  @Test
  public void testHybridInject(){
    ApplicationContext context = new AnnotationConfigApplicationContext(ComponentScanDef.class);
    RoleDataService roleDataService = (RoleDataService) context.getBean("roleDataService");
    System.out.println(roleDataService.getRole("saler"));
  }
  /********/
}
