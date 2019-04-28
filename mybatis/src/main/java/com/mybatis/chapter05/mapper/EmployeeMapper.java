package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.Employee;

public interface EmployeeMapper {
    Employee getEmployeeById(Integer id);
}
