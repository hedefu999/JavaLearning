package com.ssmr.c10.annoInject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class RoleServiceImpl implements RoleService {
  @Override
  public void printRoleInfo(Role role) {
    System.out.println(role.getName());
  }
  //@Autowired注解应用到方法上
  private Role role;
  @Autowired(required = false)
  public void setRole(Role role) {
    this.role = role;
  }
  @Bean(name = "defaultRole")
  public Role getRole(){
    Role role = new Role();
    role.setCode(200);
    role.setName("coder");
    return role;
  }
}
