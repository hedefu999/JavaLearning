package com.mybatis.chapter05.model;

import lombok.Data;

@Data
public class FemaleEmployee extends Employee {
    private FemaleHealthForm femaleHealthForm;
}
