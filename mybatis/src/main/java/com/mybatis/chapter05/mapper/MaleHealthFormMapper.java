package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.MaleHealthForm;

public interface MaleHealthFormMapper{
    MaleHealthForm getMaleHealthFormByEmpId(Integer empId);
}