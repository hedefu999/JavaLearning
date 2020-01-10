package com.jsonjava;

import lombok.Data;

import java.util.Date;
//学园都市的学生
@Data
public class AcademyStudent {
    private String name;
    private Integer age;
    private Date birth;
    private Long livingDays;
    private Double balance;
}
