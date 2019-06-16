package com.ssmr.c10.profile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProfileConfig.class)
//这样就能随时切换beans，分别用于开发测试，通常切换的是dataSource这种bean
@ActiveProfiles("dev")
public class ProfileTest {
  @Autowired
  private DataSource dataSource;
  @Test
  public void testProfile(){

  }
  //2 通过配置JVM的参数来启用对应的Profile：JAVA_OPTS="-Dspring.profiles.active=test"
  //3 在web.xml中配置profile
// <context-param>
//    <param-name>spring.profiles.active</param-name>
//    <param-value>test</param-value>
//  </context-param>
  //3 使用SpringMVC的DispatcherServlet环境参数
//  <servlet>
//    <init-param>
//      <param-name>spring.profile.active</param-name>
//      <param-value>test</param-value>
//    </init-param>
//  </servlet>
}
