package com.mybatis.chapter05.model;

import lombok.Data;

@Data
public class WorkCard {
    private Integer id;
    private Integer empId;
    private String realName;
    private String department;
}
