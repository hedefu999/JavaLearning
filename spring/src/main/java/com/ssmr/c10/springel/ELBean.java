package com.ssmr.c10.springel;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 使用SpEL获取值
 */
@Data
@Component("elBean")
public class ELBean {
  @Value("#{role}")
  private Role role;
  @Value("#{role.id}")
  private Integer id;
//  @Value("#{role.getNote().toString()}") getNote()可能得到null，.toString会抛出异常
  @Value("#{role.getNote()?.toString()}")
  private String note;
  //使用类的静态常量
  @Value("#{T(Math).PI}")
//  @Value("#{T(java.lang.Math).PI}") 在java中使用Math类不需要import，所以两种写法都可以
  private double pi;
  @Value("#{T(Math).random()}")
  private double random;
}
