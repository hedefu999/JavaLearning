package com.ssmr.txpractice.mapper;

import com.ssmr.txpractice.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper {
    int insertStudent(Student student);

    Student selectStudent(Integer id);

    int updateStudent(Student student);

    int deleteStudent(Integer id);
}
