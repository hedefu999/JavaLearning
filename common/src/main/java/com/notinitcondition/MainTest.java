package com.notinitcondition;

/**
 * 演示几种不会执行类初始化的情况
 */
public class MainTest {
    /**
     * 通过子类引用父类的静态字段，只会触发父类的初始化，而不会触发子类的初始化
     */

    public static void main(String[] args) throws Exception {
        System.out.println(Children.name);
        System.out.println(Children.class.getName());
        //new Children();
        Class.forName("com.notinitcondition.Children").newInstance();
    }
}
