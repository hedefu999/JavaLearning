package com.dpchan.c05.nondemeter;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    //Teacher仅有一个朋友类GroupLeader
    public void command(GroupLeader groupLeader){
        //Teacher类声明了非朋友类，违背了迪米特法则
        List<Girl> girlList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            girlList.add(new Girl());
        }
        groupLeader.countGirls(girlList);
    }
}
