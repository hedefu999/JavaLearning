package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.FemaleHealthForm;

public interface FemaleHealthFormMapper {
    FemaleHealthForm getFemaleHealthFormByEmpId(Integer empId);
}
