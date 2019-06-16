package com.ssmr.c10.properties;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PropertiesTest {
  public static void main(String[] args) {
    //Spring读取properties文件
    ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    String url = context.getEnvironment().getProperty("db.url");
    System.out.println(url);
  }

  @Test
  public void testPropPlaceHolder(){
    ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig2.class);
    DataSourceBean dataSourceBean = (DataSourceBean) context.getBean("dataSourceBean");
    System.out.println(dataSourceBean.getUrl());
  }

  /**
   * 使用XML配置properties文件
   */
  @Test
  public void testPropPlaceHolderInXML(){
    ApplicationContext context = new ClassPathXmlApplicationContext("classpath:com/ssmr/c10/props-placeholder.xml");
    DataSourceBean dataSourceBean = (DataSourceBean) context.getBean("dataSourceBean");
    System.out.println(dataSourceBean.getUserName());
  }

}
