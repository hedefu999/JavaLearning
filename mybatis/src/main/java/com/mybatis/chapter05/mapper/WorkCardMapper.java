package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.WorkCard;

public interface WorkCardMapper {
    WorkCard getWorkCardByEmpId(Integer empId);
}
