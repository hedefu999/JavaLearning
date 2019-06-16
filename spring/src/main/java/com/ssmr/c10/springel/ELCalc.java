package com.ssmr.c10.springel;

import org.springframework.beans.factory.annotation.Value;

/**
 * SpEL进行简单的计算
 */
public class ELCalc {
  @Value("#{role.id + 1}") //数字相加
  private int num;
  @Value("#{role.roleName + role.note}") //字符串连接
  private String str;
  @Value("#{role.id == 1}")
  private boolean equalNum;
  @Value("#{role.note eq 'teacher'}")
  private boolean equalString;
  @Value("#{role.id > 2}")
  private boolean greater;
  @Value("#{role.id < 2}")
  private boolean lower;

  /*** 三目运算 ***/
  @Value("#{role.id > 5 ? 5 : role.id}")
  private int ceil; //设置数值天花板
  // 如果为空设置默认值hello
  // role.getNote() == null ? "hello" : role.getNote()
  @Value("#{role.note ?: 'hello'}")
  private String defaultString;

  //....其他高阶功能
}
