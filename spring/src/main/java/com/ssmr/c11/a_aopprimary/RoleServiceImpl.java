package com.ssmr.c11.a_aopprimary;

import com.ssmr.c11.Role;

public class RoleServiceImpl implements RoleService {
  //SpringAOP - Pointcut 切点，被切面拦截的方法
  @Override
  public String printRole(Role role) {
    System.out.println(role.getId()+role.getName());
    return "SUCCESS";
  }
}
