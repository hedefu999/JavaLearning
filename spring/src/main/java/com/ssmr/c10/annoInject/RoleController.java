package com.ssmr.c10.annoInject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class RoleController {
  //RoleService有多个实现类，@Autowired不能确定应当注入哪一个
  @Autowired
  @Qualifier("roleService3")//直接指定注入的Bean实现类的名字
  private RoleService roleService;

  public void printRole(Role role){
    roleService.printRoleInfo(role);
  }

}
