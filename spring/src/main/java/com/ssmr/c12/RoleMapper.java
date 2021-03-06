package com.ssmr.c12;

import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper {
    Role getRole(Integer id);
    int saveRole(String name, Integer age, String note);
    int saveEntireRole(Role role);
}
