package com.concurrency.miscellaneous;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.util.Objects;

/**
 * java 对象布局
 * 作为Mark Word的前导知识
 * refto https://blog.csdn.net/baidu_28523317/article/details/104453927
 */
public class JavaObjectLayout {
    static class Student {
        private String name;
        private Integer age;
        private Boolean married;

        public Student(String name, Integer age, boolean sex) {
            this.name = name;
            this.age = age;
            this.married = sex;
        }
    }

    public static void main(String[] args) {
        Student user = new Student("jack is a good man",12,false);
        System.identityHashCode(user);
        String s = ClassLayout.parseInstance(user).toPrintable();
        System.out.println(s);
        System.out.println(Integer.toBinaryString(user.hashCode()));
        System.out.println(Integer.toHexString(user.hashCode()));
        Object instance = new Object();
        System.identityHashCode(instance);
        ClassLayout instanceLayout = ClassLayout.parseInstance(instance);
        System.out.println(instanceLayout.toPrintable());
        System.out.println(Integer.toHexString(instance.hashCode()));
    }

}
