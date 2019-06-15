package com.dpchan.c02.parentchildclass;

public class Child extends Parent {
    @Override
    public void methodA(ChildParam childParam) {
        super.methodA(childParam);
        System.out.println("子类的覆写方法");
    }
    //@Override 子类方法扩大入参范围，就是重载，加override就是重写，会报错
    public void methodA(ParentParam childParam) {
        System.out.println("子类的重载方法");
    }

}
