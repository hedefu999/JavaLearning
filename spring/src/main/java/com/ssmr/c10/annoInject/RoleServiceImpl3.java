package com.ssmr.c10.annoInject;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component("roleService3")
@Primary //接口存在多个实现类时，使用@Primary标识这个实现类是优先注入的
public class RoleServiceImpl3 implements RoleService {
  @Override
  public void printRoleInfo(Role role) {
    System.out.println(role.getCode());
  }
}
