package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.WorkCard;

public interface WorkCardMapper {
    int simpleInsert(WorkCard workCard);
    int insertIfNotExists(WorkCard workCard);
    int insertOnDuplicateKeyUpdate(WorkCard workCard);
    WorkCard getWorkCardByEmpId(Integer empId);
}
