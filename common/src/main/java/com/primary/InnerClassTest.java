package com.primary;

public class InnerClassTest {
    /**
     非静态内部类不允许有static成员变量或方法.
     否则将出现 Outer.Inner.staticField 访问方式
     企图在Wrapper Inner均未初始化时访问成员变量
     */
    static class Wrapper{
        public static String wrapperName = "jacky";
        public class Inner{
            public /*static*/ String name = "jack";
            public final Integer age = 12;
            public /*static*/ String getInfo(){
                return "name";//"name "+ age;
            }
        }
        static class Inner2{
            public static String name = "lucy";
            public static String getInfo(){
                return name;
            }
        }

        public static void main(String[] args) {
            //Inner.name
            //Wrapper.Inner.name;
        }
    }
}
