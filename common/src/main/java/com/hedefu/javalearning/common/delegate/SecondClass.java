package com.hedefu.javalearning.common.delegate;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SecondClass {
    private CustomDelegate delegate;

    public SecondClass(CustomDelegate delegate) {
        this.delegate = delegate;
        this.begin();
    }

    public void begin(){
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                delegate.setValue(getNowMillis());
            }
        };
        long delay = 0;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, delay, 1000);


        //org.apache.commons.lang3.concurrent.BasicThreadFactory
        //ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
        //        new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
        //
        //executorService.scheduleAtFixedRate(new Runnable() {
        //    @Override
        //    public void run() {
        //        //do something
        //    }
        //},3000,2000, TimeUnit.HOURS);


    }
    private String getNowMillis(){
        return String.valueOf(System.currentTimeMillis());
    }
}
