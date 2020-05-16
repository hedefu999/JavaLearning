package com.concurrency.javaconcurrencyart;

public class _03JavaMemoryModel {
    //static class VolatileBarrierExample{
    //    int a;
    //    volatile int v1 = 1;
    //    volatile int v2 = 2;
    //    void readAndWrite() {
    //        int i = v1;    // 第一个volatile读
    //        int j = v2;      // 第二个volatile读
    //        a = i + j;         // 普通写
    //        v1 = i + 1;      // 第一个volatile写
    //        v2 = j * 2;      // 第二个 volatile写
    //    }
    //}
    static int a;
    static volatile int v1 = 1;
    static volatile int v2 = 2;
    public static void main(String[] args) {
        int i = v1;    // 第一个volatile读
        int j = v2;      // 第二个volatile读
        a = i + j;         // 普通写
        v1 = i + 1;      // 第一个volatile写
        v2 = j * 2;      // 第二个 volatile写
    }

    static class FinalFieldsMemorySemantics{
        static class FinalExample {
            int i;         // 普通变量
            final int j;       // final变量
            static FinalExample obj;
            public FinalExample () {       // 构造函数
                i = 1;       // 写普通域
                j = 2;      // 写final域
            }
            public static void writer () {  // 写线程A执行
                obj = new FinalExample ();
            }
            public static void reader () {   // 读线程B执行
                FinalExample object = obj;  // 读对象引用
                int a = object.i;  // 读普通域
                int b = object.j;  // 读final域
            }
        }
    }











}
