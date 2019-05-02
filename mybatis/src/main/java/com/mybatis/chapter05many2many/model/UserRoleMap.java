package com.mybatis.chapter05many2many.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleMap implements Serializable{
    private static final long serialVersionUID = -5741180817561459409L;
    private Integer id;
    private Integer userId;
    private Integer roleId;
}
