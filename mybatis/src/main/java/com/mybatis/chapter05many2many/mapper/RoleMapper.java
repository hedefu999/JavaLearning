package com.mybatis.chapter05many2many.mapper;

import com.mybatis.chapter05many2many.model.Role;

public interface RoleMapper {
    Role getRoleUsers(Integer roleId);
}
