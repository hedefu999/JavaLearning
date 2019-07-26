package com.hedefu.javalearning.common.delegate;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class TestDelegate {
    public static void main(String[] args) {
        //FirstClass firstClass = new FirstClass();
        //firstClass.beginRunSecondDelegateMethod();







        //org.apache.commons.lang3.concurrent.BasicThreadFactory
        BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,threadFactory);
        //这种写法后面定时任务没有执行


        ScheduledExecutorService executorService1 = Executors.newScheduledThreadPool(4);





        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        //Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(5, 200,0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        pool.execute(()-> System.out.println(Thread.currentThread().getName()));
        pool.shutdown();//gracefully shutdown




        executorService1.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行一次"+System.currentTimeMillis());
            }
        },0,2000, TimeUnit.MILLISECONDS);
        //executorService.sta
    }

    public static void print(){

    }
}
