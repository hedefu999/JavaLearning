package com.ssmr.c10.xmlInject;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;
@Data
public class UserRoleAssemble {
  private List<Role> roleList;
  private Map<Role,User> map;
  private Set<Role> roleSet;
}
