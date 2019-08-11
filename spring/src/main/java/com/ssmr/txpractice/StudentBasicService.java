package com.ssmr.txpractice;

public interface StudentBasicService {
    int saveStudent(Student student);

    Student selectStudent(Integer id);

    int updateStudent(Student student);

    int deleteStudent(Integer id);
}
