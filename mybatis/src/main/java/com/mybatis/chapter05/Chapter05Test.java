package com.mybatis.chapter05;

import com.mybatis.chapter05.mapper.*;
import com.mybatis.chapter05.model.*;
import com.mybatis.utils.JSONUtils;
import com.mybatis.utils.SqlSessionFactoryUtils;
import org.junit.Test;

import java.util.List;

public class Chapter05Test {
    //演示一个全表关联的查询
    @Test
    public void test1(){
        EmployeeMapper mapper = SqlSessionFactoryUtils.getMapper(EmployeeMapper.class, "com/mybatis/chapter05/mapper/mybatis-config.xml");
        Employee employee = mapper.getEmployeeById(1);
        //测试中发现taskList与healthForm总是空---
        //EmployeeTaskMap的查询指定了resultType导致级联属性不能初始化
        //healthForm空的问题：数据错误，在male_healthform中存性别为female的员工数据，员工ID错误
        System.out.println(employee.getId());
        //((MaleEmployee)employee).getMaleHealthForm();
        //employee.getWorkCard();
        employee.getTaskList().get(0).toString();
    }
    @Test
    public void testAllInOneResult(){
        EmployeeMapper mapper = SqlSessionFactoryUtils.getMapper(EmployeeMapper.class, "com/mybatis/chapter05/mapper/mybatis-config.xml");
        Employee employee = mapper.getEmployeeInfoById(1);
        System.out.println(employee.getTaskList());
    }

    @Test
    public void test2(){
        EmployeeTaskMapper mapper = SqlSessionFactoryUtils.getMapper(EmployeeTaskMapper.class,"com/mybatis/chapter05/mapper/mybatis-config.xml");
        List<EmployeeTask> taskList = mapper.getEmpTaskByEmpId(1);
        System.out.println(taskList.size());
        System.out.println(taskList.get(0).getId());
        System.out.println(taskList.get(0).getTask().toString());
    }

    @Test
    public void test42(){
        WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,"jdbc.properties");
        WorkCard workCard = workCardMapper.getWorkCardByEmpId(12);
        System.out.println(workCard.getDepartment());
    }

    static class OneQueryTask implements Runnable{
        @Override
        public void run() {
            String configFileRPath = "com/mybatis/chapter05/mapper/mybatis-config.xml";
            String jdbcFileRPath = "jdbc.properties";
            Integer empId = 13;
            WorkCardMapper workCardMapper = SqlSessionFactoryUtils.getMapper2(WorkCardMapper.class,jdbcFileRPath);
            //查询是否存在
            WorkCard workCard = workCardMapper.getWorkCardByEmpId(empId);
            if (workCard == null){
                workCard = new WorkCard();
                workCard.setEmpId(empId);
                workCard.setRealName("lucy");
                workCard.setDepartment("tianqin");
                //不存在就初始化
                String currentThreadName = Thread.currentThread().getName();
                System.out.println(currentThreadName+" 开始执行初始化操作");
                try {
                    //workCardMapper.simpleInsert(workCard); //A方案
                    workCardMapper.insertIfNotExists(workCard);//B方案
                    //workCardMapper.insertOnDuplicateKeyUpdate(workCard);//C方案
                }catch (Exception e){
                    System.out.println(currentThreadName + " 操作发生异常："+ e.getMessage());
                }
                System.out.println(workCard.getId());
            }
        }
        public static void main(String[] args) {
            OneQueryTask oneQueryTask = new OneQueryTask();
            Thread thread1 = new Thread(oneQueryTask);thread1.setName("A线程");
            Thread thread2 = new Thread(oneQueryTask);thread2.setName("B线程");
            thread1.start();
            thread2.start();
        }
        /**
         * 演示各种insert方式在并发场景下必然发生唯一索引冲突时的健壮性
         * A方式 无数据时会有一个线程抛出异常（是A是B不确定），正常插入一条数据。再次执行就是两个异常；
         * B方式 能够插入数据，但第二个插入数据的线程极易发生死锁 Deadlock found when trying to get lock; try restarting transaction，也有不发生死锁的情况
         * C方案 正常操作，只有一个线程能正常插入数据，且其他线程调用插入方法也不会抛出异常，当据说这种SQL在某些版本存在死锁的BUG
         */
    }




}
