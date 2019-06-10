package com.dpchan.c05;

import com.dpchan.c05.nondemeter.Girl;
import com.dpchan.c05.nondemeter.GroupLeader;
import com.dpchan.c05.nondemeter.Teacher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Client {
    @Test
    public void testNonDemeter(){
        Teacher teacher = new Teacher();
        teacher.command(new GroupLeader());
    }
    @Test
    public void testDemeter(){
        List<Girl> girls = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            girls.add(new Girl());
        }
        com.dpchan.c05.demeter.Teacher teacher = new com.dpchan.c05.demeter.Teacher();
        teacher.command(new com.dpchan.c05.demeter.GroupLeader(girls));
    }
}
