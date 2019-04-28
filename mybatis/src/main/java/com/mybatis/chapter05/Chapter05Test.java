package com.mybatis.chapter05;

import com.mybatis.chapter05.mapper.EmployeeMapper;
import com.mybatis.chapter05.mapper.MaleHealthFormMapper;
import com.mybatis.chapter05.model.Employee;
import com.mybatis.chapter05.model.HealthForm;
import com.mybatis.chapter05.model.MaleEmployee;
import com.mybatis.chapter05.model.MaleHealthForm;
import com.mybatis.utils.JSONUtils;
import com.mybatis.utils.SqlSessionFactoryUtils;
import org.junit.Test;

public class Chapter05Test {
    //演示一个全表关联的查询
    @Test
    public void test1(){
        EmployeeMapper mapper = SqlSessionFactoryUtils.getMapper(EmployeeMapper.class, "com/mybatis/chapter05/mapper/mybatis-config.xml");
        Employee employee = mapper.getEmployeeById(1);
        //测试中发现taskList与healthForm总是空---
        //EmployeeTaskMap的查询指定了resultType导致级联属性不能初始化
        //healthForm空的问题：数据错误，在male_healthform中存性别为female的员工数据，员工ID错误
        System.out.println(JSONUtils.toString(employee));

        //TODO 完善本部分笔记
    }

    @Test
    public void test2(){

    }

}