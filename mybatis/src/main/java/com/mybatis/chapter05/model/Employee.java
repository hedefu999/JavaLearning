package com.mybatis.chapter05.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Employee {
    private Integer id;
    private String name;
    private Integer sex;
    //java.sql.Date与java.util.Date对比：https://www.baeldung.com/java-util-date-sql-date
    //结论：最好使用LocalDate！
    private Date birthday;

    //employee 一对一 workCard
    private WorkCard workCard;
    //employee 一对多 employeeTask
    private List<EmployeeTask> taskList;
}
