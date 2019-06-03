package com.ssmr.c10.annoInject;

import org.springframework.beans.factory.annotation.Autowired;

public class RoleController2 {
  private RoleService roleService;
  //@Autowired放到构造函数的参数前面实现注入
  public RoleController2(@Autowired RoleService roleService){
    this.roleService = roleService;
  }
}
