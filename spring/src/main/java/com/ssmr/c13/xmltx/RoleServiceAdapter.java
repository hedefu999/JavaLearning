package com.ssmr.c13.xmltx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("roleServiceAdapter")
public class RoleServiceAdapter implements RoleService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int saveRole() {
        System.out.println("role service adapter");
        int result = jdbcTemplate.update(sql, "service adapter", 4, "not support tx");
        int i = 1/0;
        return result;
    }
}
