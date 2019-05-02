package com.mybatis.chapter05many2many.model;

import lombok.Data;

import java.util.List;

@Data
public class Role {
    private Integer id;
    private String name;

    List<User> users;
}
