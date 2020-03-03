package com.concurrency.javadxcbchxjs;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class _9ThreadLocalAPI {
    public static long timeStamp(){
        return System.currentTimeMillis()%10000;
    }
    public static String threadName(){
        return Thread.currentThread().getName();
    }

    static class TestThreadLocal{
        static class ThreadLocalExt extends ThreadLocal{
            @Override
            protected Object initialValue() {
                return new Date().getTime();
            }
        }
        public static void main(String[] args) throws InterruptedException {
            ThreadLocalExt threadLocalExt = new ThreadLocalExt();
            Thread a = new Thread(){
                @Override
                public void run() {
                    System.out.println(threadName()+threadLocalExt.get());
                }
            };
            Thread b = new Thread(){
                @Override
                public void run() {
                    System.out.println(threadName()+threadLocalExt.get());
                }
            };
            a.start();
            TimeUnit.SECONDS.sleep(2);
            System.out.println(threadName()+threadLocalExt.get());
            b.start();
            /**
             * Thread-01581605987461
             * main1581605989465
             * Thread-11581605989465
             * ThreadLocal不光隔离各线程自己的变量，还能隔离默认变量
             */
        }
    }

    /**
     * @title 父子线程的值继承
     */
    static class SimpleThreadLocal{
        static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
        public static void main(String[] args) {
            threadLocal.set("main"); //A
            new Thread(){
                @Override
                public void run() {
                    System.out.println(threadLocal.get());
                }
            }.start();
            //B
            System.out.println(threadLocal.get());
        }
        /**
         * 打印结果：
         * main
         * main
         * 如果将A行的代码移到B处：
         * main
         * null  似乎这是因为父线程还没设置，所以取不到
         */
    }

    //采用另外一种父子线程的写法
    static class AnotherFormOfParentChildThread{
        static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
        static class ParentThread extends Thread{
            @Override
            public void run() {
                new Thread(){
                    @Override
                    public void run() {
                        threadLocal.set("child");
                        System.out.println("chile thread : "+ threadLocal.get());
                    }
                }.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("parent thread : "+ threadLocal.get());
            }
        }
        public static void main(String[] args) {
            new ParentThread().start();
        }
        /**
         * chile thread : child
         * parent thread : null
         * child设置的值parent拿不到
         */
    }

    static class InheritableThreadLocalTest{
        static class InheritableThreadLocalExt extends InheritableThreadLocal<String>{
            @Override
            protected String initialValue() {
                return "parent_default"; //设置父线程的默认值
            }
            @Override
            protected String childValue(String parentValue) {
                System.out.println("parentValue = "+parentValue);
                String childValue = "child_default"; //设置子线程的默认值，可以选择与父线程相同
                System.out.println("childValue = "+childValue);
                return childValue;
            }
        }
        public static void main(String[] args) throws InterruptedException {
            InheritableThreadLocalExt localExt = new InheritableThreadLocalExt();
            System.out.println("父线程："+ localExt.get()); //A
            //Thread.sleep(1000);
            new Thread(){
                @Override
                public void run() {
                    System.out.println("子线程："+ localExt.get());
                }
            }.start(); //B
        }
        /**
         * -- B 早于 A 执行，ThreadLocal的childValue方法没有执行
         * 父线程：parent_default
         * 子线程：parent_default
         *
         * -- A 早于 B 执行：
         * 父线程：parent_default
         * parentValue = parent_default
         * childValue = child_default
         * 子线程：child_default
         */
    }

}
