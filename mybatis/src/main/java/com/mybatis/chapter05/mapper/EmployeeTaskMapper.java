package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.EmployeeTask;

import java.util.List;

public interface EmployeeTaskMapper {
    List<EmployeeTask> getEmpTaskByEmpId(Integer empId);
}
