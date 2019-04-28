package com.mybatis.chapter05.model;

import lombok.Data;

@Data
public class MaleEmployee extends Employee {
    private MaleHealthForm maleHealthForm;
}
