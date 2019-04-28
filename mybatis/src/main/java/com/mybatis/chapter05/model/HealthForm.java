package com.mybatis.chapter05.model;

import lombok.Data;

@Data
public class HealthForm {
    //INT(X)的取值范围是-2147483648~2147483647/0~4294967295
    //Integer int 4B 范围-2147483648~2147483647，Java没有无符号整数
    private Integer id;
    private Integer empId;
    private String heart;

}
