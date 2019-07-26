package com.ssmr.c11.c_xmlaop;

import com.ssmr.c11.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
  @Override
  public void printRole(Role role) {
    System.out.printf("入参：id = %d, name = %s%n", role.getId(), role.getName());
  }

  @Override
  public void printRole2(Role role2) {
    System.out.printf("第二个打印函数：id=%d,name=%s\n",role2.getId(),role2.getName());
  }

  @AnnoAop("Daniel")
  @Override
  public void printRole3(Role role3) {
    System.out.printf("带有注解的的打印函数：role.name = %s.\n", role3.getName());
  }

  @Override
  public void printRole4(Role role4) {
    System.out.printf("打印数据：role.name = %s.\n",role4.getName());
  }


}
