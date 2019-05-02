package com.mybatis.chapter05many2many.mapper;

import com.mybatis.chapter05many2many.model.ProcedureModel;
import org.apache.ibatis.annotations.Param;

public interface ProcedureMapper {
    ProcedureModel callPrimaryProc(ProcedureModel model);
}
