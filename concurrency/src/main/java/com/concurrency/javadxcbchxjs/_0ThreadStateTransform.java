package com.concurrency.javadxcbchxjs;

/**
 * @title 线程状态转换研究
 * @desc 线程状态定义见Thread.State
 * 有NEW RUNNABLE BLOCKED WAITING TIMED_WAITING TERMINATED
 */
public class _0ThreadStateTransform {
    static class BasicTest{
        static class MyThread extends Thread{
            public MyThread(){
                System.out.println("构造方法中的状态："+Thread.currentThread().getState());
            }
            @Override
            public void run() {
                System.out.println("run方法中的状态："+Thread.currentThread().getState());
            }
        }
        public static void main(String[] args) {
            try {
                MyThread myThread = new MyThread();
                System.out.println("main方法中的状态1："+myThread.getState());
                Thread.sleep(1000);
                myThread.start();
                Thread.sleep(1000);
                System.out.println("main方法中的状态2："+myThread.getState());
            }catch (Exception e){}
        }
        /**
         * 构造方法中打印出的状态实际上是main主线程的状态
         *
         * 构造方法中的状态：RUNNABLE
         * main方法中的状态1：NEW
         * run方法中的状态：RUNNABLE
         * main方法中的状态2：TERMINATED
         */
    }
}
