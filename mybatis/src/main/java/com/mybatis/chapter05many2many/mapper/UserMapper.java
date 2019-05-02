package com.mybatis.chapter05many2many.mapper;

import com.mybatis.chapter05many2many.model.User;

import java.util.List;

public interface UserMapper {
    User getUserRoles(Integer userId);
    List<User> getUsersByRoleId(Integer roleId);
}
