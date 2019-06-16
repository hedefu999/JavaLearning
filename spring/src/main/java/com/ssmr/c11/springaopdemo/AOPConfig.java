package com.ssmr.c11.springaopdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.ssmr.c11.springaopdemo")
public class AOPConfig {
  @Bean
  public RoleAspect getRoleAspect(){
    return new RoleAspect();
  }
}
/**
 * 使用XML定义切面
 * <beans xmlns:aop="http://www.springframework.org/schema/aop"
 * xsi:schemaLocation="http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.0.xsd">
 * <aop:aspectj-autoproxy/> 效果与@EnableAspectJAutoProxy等同
 */
