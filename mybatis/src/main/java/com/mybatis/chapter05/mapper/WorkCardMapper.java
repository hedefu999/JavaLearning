package com.mybatis.chapter05.mapper;

import com.mybatis.chapter05.model.WorkCard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WorkCardMapper {
    int simpleInsert(WorkCard workCard);
    int insertIfNotExists(WorkCard workCard);
    int insertOnDuplicateKeyUpdate(WorkCard workCard);
    WorkCard getWorkCardByEmpId(Integer empId);
    WorkCard getByPessimisticId(Integer empId);

    @Select("select id , emp_id as empId, real_name as realName , department " +
            "from work_card where emp_id = #{empId} and department = #{department}") // for update
    WorkCard getByOptimisticEmpIdAndDepartName(@Param("empId") Integer empId, @Param("department") String department);

    @Update("update work_card set emp_id = #{workCard.empId}, real_name = #{workCard.realName},department = #{workCard.department} " +
            "where id = #{workCard.id}")
    int updateByPrimaryKey(@Param("workCard") WorkCard workCard);
    @Update("update work_card set real_name = concat(real_name, ',', #{workCard.realName}),department = #{workCard.department} where id = #{workCard.id}")
    int appendRealNameAndUpdateDepartById(@Param("workCard") WorkCard workCard);
}
