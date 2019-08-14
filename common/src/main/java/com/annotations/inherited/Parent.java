package com.annotations.inherited;

@InheritedTest("使用Inherited的注解 class")
@InheritedTest2("未使用Inherited的注解 class")
public class Parent {
    @InheritedTest("使用Inherited的注解 method")
    @InheritedTest2("未使用Inherited的注解 method")
    public void method(){

    }
    @InheritedTest("使用Inherited的注解 method2")
    @InheritedTest2("未使用Inherited的注解 method2")
    public void method2(){

    }

    @InheritedTest("使用Inherited的注解 field")
    @InheritedTest2("未使用Inherited的注解 field")
    public String a;
}
