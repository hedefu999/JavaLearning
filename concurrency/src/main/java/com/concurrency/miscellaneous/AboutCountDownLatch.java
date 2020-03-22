package com.concurrency.miscellaneous;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AboutCountDownLatch {
    public static void main(String[] args) throws Exception {
        final CountDownLatch latch = new CountDownLatch(3);
        new Thread(){
            public void run() {
                System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) { }
                System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                latch.countDown();
        }}.start();
        new Thread(){
            public void run() {
                System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) { }
                System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                latch.countDown();
        }}.start();
        System.out.println("等待 2 个子线程执行完毕...");
        //latch.await();
        latch.await(5, TimeUnit.SECONDS);
        System.out.println("2 个子线程已经执行完毕");
        System.out.println("继续执行主线程");
    }
}
