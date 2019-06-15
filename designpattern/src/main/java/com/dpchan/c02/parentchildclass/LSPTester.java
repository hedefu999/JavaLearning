package com.dpchan.c02.parentchildclass;

public class LSPTester {
    public static void main(String[] args) {
        ChildParam childParam = new ChildParam();
        ParentParam parentParam = new ParentParam();

        Parent parent = new Parent();
        parent.methodA(childParam);//父类方法
        System.out.println("-=-=-=-=-=-=-=");
        Parent fakeParent = new Child();//这种方式调不到子类的重载方法
        fakeParent.methodA(childParam);//父类方法 子类的覆写方法
        System.out.println("-=-=-=-=-=-=-=");
        Child child = new Child();
        child.methodA(parentParam);//子类的重载方法
    }
}
