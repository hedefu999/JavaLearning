package com.mybatis.chapter05many2many.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private Integer id;
    private String name;

    private List<Role> roles;
}
