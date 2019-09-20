package com.utils;

public class PageHelper {
    private int total = 0;//总条数
    private int pageCount = 0;//总页数
    private int pageSize = 10;//每页显示行数  ①如果要调整每页显示行数，必须第一个设置
    private int pageIndex = 1;//当前页

    public PageHelper(int total, int pageSize) {
        this.total = total;
        this.pageSize = pageSize;
    }

    public int getPageCount(){
        return total%pageSize==0?total/pageSize:total/pageSize+1;
    }

    public int getPageStart(int pageIndex){
        return (pageIndex-1)*pageSize+1;
    }
    public int getPageEnd(int pageIndex){
        return pageIndex*pageSize;
    }
}
