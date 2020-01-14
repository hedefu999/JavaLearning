package com.mybatis.chapter05.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkCard {
    private Integer id;
    private Integer empId;
    private String realName;
    private String department;
}
