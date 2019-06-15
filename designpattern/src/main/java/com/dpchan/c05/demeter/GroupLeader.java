package com.dpchan.c05.demeter;

import com.dpchan.c05.nondemeter.Girl;

import java.util.List;

public class GroupLeader {
    private List<Girl> girlList;
    public GroupLeader(List<Girl> _listGirls){
        this.girlList = _listGirls;
    }
    public void countGirls(){
        System.out.println("女生数量是："+ this.girlList.size());
    }
}
