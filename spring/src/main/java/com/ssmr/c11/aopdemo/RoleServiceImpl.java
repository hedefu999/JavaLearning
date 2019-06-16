package com.ssmr.c11.aopdemo;

import com.ssmr.c11.Role;

public class RoleServiceImpl implements RoleService {
  //SpringAOP - Pointcut
  @Override
  public void printRole(Role role) {
    System.out.println(role.getId()+role.getName());
  }
}
