package com.ssmr.c13.xmltx;

public interface RoleService {
    String sql = "insert into role(name, age, note) VALUE(?,?,?)";
    int saveRole();
}
