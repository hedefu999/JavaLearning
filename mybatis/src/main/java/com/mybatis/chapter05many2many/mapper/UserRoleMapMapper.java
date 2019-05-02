package com.mybatis.chapter05many2many.mapper;

import com.mybatis.chapter05many2many.model.UserRoleMap;

public interface UserRoleMapMapper {
    UserRoleMap getById(Integer id);
    int updateUserRoleMapById(UserRoleMap map);
}
