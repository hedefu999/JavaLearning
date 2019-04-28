package com.mybatis.chapter05.model;

import lombok.Data;

@Data
public class EmployeeTask {
    private Integer id;
    private Integer empId;
    //private Integer taskId;
    private String taskName;

    //employeeId - taskId 一对多
    private Task task;
}
