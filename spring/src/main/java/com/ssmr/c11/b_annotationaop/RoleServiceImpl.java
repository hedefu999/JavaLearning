package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
  @Override
  public void printRole(Role role) {
    System.out.println(role.getId()+role.getName());
  }

  @Override
  public void printRole2(Role role) {
    System.out.printf("编号：%s，名字：%s。", role.getId(), role.getName());
  }

  @Override
  public void printRole3(Role role, Integer code) {
    System.out.printf("入参role是：%s，code = %d%n", role.toString(), code);
  }
}
