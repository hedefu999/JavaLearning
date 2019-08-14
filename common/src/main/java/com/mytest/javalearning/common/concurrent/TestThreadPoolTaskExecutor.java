package com.mytest.javalearning.common.concurrent;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class TestThreadPoolTaskExecutor {
    public static void main(String[] args) {
        init();
    }
    private static void init(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setKeepAliveSeconds(120);
        executor.setQueueCapacity(32);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        List<Future<String>> taskResults = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            taskResults.add(process(executor,i));
        }
        while (true){
            boolean isAllDone = true;
            for (Future<String> taskResult : taskResults){
                boolean isTerminated = taskResult.isDone() || taskResult.isCancelled();
                isAllDone = isAllDone && isTerminated;
            }
            if (isAllDone){ //此处认为任务都执行完成？？？
                break;
            }
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        for (Future<String> taskResult : taskResults) {
            String ex;
            try {
                ex = taskResult.get();
            }catch (Exception e){
                ex = e.getMessage();
            }
            System.out.println(ex);
        }
    }

    private static Future<String> process(final ThreadPoolTaskExecutor executor, final int k){
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep((k+1)*500);
                }catch (Exception e){
                    return e.getMessage();
                }
                return "k= "+k+" success";
            }
        };
        Future<String> submit = executor.submit(callable);
        return submit;
    }
}
