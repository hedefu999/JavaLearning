package com.ssmr.txpractice;

import java.util.List;

public interface BizService {
    void batchInsertRole(List<Student> students, List<Role> roleList);
    void insertBoth(Student student, Role role);

    void insertBoth2(Student student, Role role);

    void insertBoth3(List<Student> students, List<Role> roles);
}
