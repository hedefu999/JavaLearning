package com.mytest.mybatis.mapper;

import com.mytest.mybatis.model.Role;
import com.mybatis.chapter08.PageParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {
    //要让mybatis自己去找字段就不要加@Param
    int insertRole(Role role);

    int deleteRole(Role role);
    //使用了@Param就要role.id
    int deleteRole2(@Param("role") Role role);

    int updateRole(Role role);

    Role queryWithConvertedColumn(Long id);
    Role queryWithResultMap(Long id);
    //注解方式创建映射器，XML方式优先级高于注解
    @Select("select id,role_name as roleName,note from role where id = #{id}")
    Role queryRoleWithAnno(Long id);
    //@Param不加会提示names找不到
    List<Role> findRoles(@Param("pageParam")PageParam pageParam, @Param("names") List<String> names);

    List<Role> getRoles4Plugin(@Param("pageParam")PageParam pageParam,@Param("fromIndex")int fromIndex);
}
