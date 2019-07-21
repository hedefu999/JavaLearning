package com.ssmr.c11.aopdemo;

import com.ssmr.c11.Role;

public class RoleServiceImpl implements RoleService {
  //SpringAOP - Pointcut
  @Override
  public String printRole(Role role) {
    System.out.println(role.getId()+role.getName());
    return "SUCCESS";
  }
}
