package com.ssmr.c11.springaopdemo;

import com.ssmr.c11.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
  @Override
  public void printRole(Role role) {
    System.out.println(role.getId()+role.getName());
  }
}
