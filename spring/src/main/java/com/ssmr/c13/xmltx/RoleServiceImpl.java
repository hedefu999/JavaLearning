package com.ssmr.c13.xmltx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int saveRole() {
        System.out.println("role service impl");
        int result = jdbcTemplate.update(sql, "service impl", 5, "support tx,you cant see me");
        int i = 1/0;
        return result;
    }
}
