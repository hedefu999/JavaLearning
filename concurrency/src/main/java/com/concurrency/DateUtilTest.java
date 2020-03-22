package com.concurrency;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DateUtilTest {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(){
            @Override
            public void run() {
                DateUtil.format(new Date(), "yyyy-MM-dd");
            };
        };

        Thread t2 = new Thread(){
            @Override
            public void run() {
                DateUtil.format(new Date(), "yyyy-MM-dd");
            };
        };

        Thread t3 = new Thread(){
            @Override
            public void run() {
                DateUtil.format(new Date(), "yyyy-MM-dd");
            };
        };

        Thread t4 = new Thread(){
            @Override
            public void run() {
                try{
                    DateUtil.parse("2017-06-10 12:00:01", "yyyy-MM-dd HH:mm:ss");
                }catch(ParseException e){
                    e.printStackTrace();
                }

            };
        };

        Thread t5 = new Thread(){
            @Override
            public void run() {
                try{
                    DateUtil.parse("2017-06-10 12:00:01", "yyyy-MM-dd HH:mm:ss");
                }catch(ParseException e){
                    e.printStackTrace();
                }

            };
        };


        System.out.println("单线程执行：");
        ExecutorService exec1 = Executors.newFixedThreadPool(1);
        exec1.execute(t1);
        exec1.execute(t2);
        exec1.execute(t3);
        exec1.execute(t4);
        exec1.execute(t5);
        exec1.shutdown();

        Thread.sleep(1000);

        System.out.println("双线程执行：");
        ExecutorService exec2 = Executors.newFixedThreadPool(2);
        exec2.execute(t1);
        exec2.execute(t2);
        exec2.execute(t3);
        exec2.execute(t4);
        exec2.execute(t5);
        exec2.shutdown();
    }
}
