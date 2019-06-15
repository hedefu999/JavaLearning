package com.dpchan.c05.demeter;

public class Teacher {
    //迪米特法则将Girl类下放到GroupLeader中，不再声明这个类，也不会import
    public void command(GroupLeader groupLeader){
        groupLeader.countGirls();
    }
}
