package com.concurrency.starter;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class _4basicconcurrencyapi {
    /**
     * isAlive()方法 判断当前线程是否处于活动状态:线程处于正在运行或准备开始运行的状态
     */
    static class ExtendsThread extends Thread{
        @Override
        public void run() {
            System.out.println(this.isAlive());//true
        }

        public static void main(String[] args) throws InterruptedException {
            ExtendsThread extendsThread = new ExtendsThread();
            System.out.println("begin == "+ extendsThread.isAlive());//false
            extendsThread.start();
            Thread.sleep(1000);//添加这一行后下一行输出 false 因为extendsThread已经start在1秒内执行完毕了
            System.out.println("end == " + extendsThread.isAlive());//true
        }

        static class CountOperate extends Thread{
            public CountOperate(){
                System.out.println("countoperate -- begin");
                print();
                System.out.println("countoperate -- end");
            }

            @Override
            public void run() {
                System.out.println("run -- begin");
                print();
                System.out.println("run -- end");
            }
            private void print(){
                Thread thread = Thread.currentThread();
                System.out.printf("currentThread: name = %s，Id = %s, alive = %s\n",thread.getName(),thread.getId(), thread.isAlive());
                System.out.printf("this: name = %s,Id = %s, alive = %s\n",this.getName(),this.getId(), this.isAlive());
            }
            public static void main(String[] args) {
                CountOperate c = new CountOperate();
                Thread t1 = new Thread(c, "A");
                System.out.println("main begin t1 isAlive = " + t1.isAlive());
                t1.start();
                System.out.println("main end t1 isAlive = " + t1.isAlive());
            }
            /*
            * countoperate -- begin
            currentThread: name = main, alive = true
            this: name = Thread-0, alive = false
            countoperate -- end
            main begin t1 isAlive = false
            main end t1 isAlive = true
            run -- begin
            currentThread: name = A, alive = true
            this: name = Thread-0, alive = false
            run -- end*/
            /**
             * 在线程类内this.getName()始终是Thread-0...
             */
            /*
            *  -- begin
            *   main true
            *   main true
            *   -- end
            *   = false
            *   run -- begin
            *   A true
            *   A true
            *   run -- end
            *   = false
            * */
        }

        static class SleepInRun extends Thread{
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis());
            }
            public static void main(String[] args) {
                SleepInRun sleepInRun = new SleepInRun();
                System.out.println("main start");
                sleepInRun.start();
                //sleepInRun.run(); 调run会把main阻塞掉
                System.out.println("main end");
            }
        }

        /**
         * stop() method are not supposed to use, it will be removed in future java version
         * 在java中有3种方法中止正在运行的线程
         * 1。使用退出标志，是线程正常run方法执行完成后退出
         * 2. 使用interrupt()
         * 3.stop()
         *
         * 判断线程是否中断的两种方法
         * Thread.interrupted() static方法，线程是否被中断过,调用一次会清除中断，所以连续第二次调用总是返回false
         * this.isInterrupted() 测试线程是否是中断状态，但不清除状态标志
         */
        static class InterruptThread extends Thread{
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    System.out.println("i = "+i);
                }
            }
            public static void main(String[] args) {
                try {
                    //打印结果 难以理解。。。
                    InterruptThread thread = new InterruptThread();
                    thread.interrupt();
                    System.out.println("是否停止1？+"+thread.isInterrupted());
                    Thread.sleep(1000);
                    thread.start();
                    System.out.println("是否停止2？+"+thread.isInterrupted());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        private static void timeCosumedOperation(){
            long start = System.currentTimeMillis();
            String baidu = "https://www.guancha.cn/";
            try {
                URL url = new URL(baidu);
                URLConnection urlConn = url.openConnection();
                InputStream stream = urlConn.getInputStream();
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = stream.read(bytes)) != -1){
                    String str = new String(bytes, 0, len);
                    if (str.contains("中方")){
                        System.out.println(stream.available());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            System.out.println("耗时："+ (end - start));
        }

        public static void fiveTimesOperation(){
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis()-start<5000){
            }
        }

        public static void fiveSecondsWait(){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
        }
        public static void twoSecondsWait(){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
        }

        //1.7.3 能停止的线程--异常法
        static class StopByInterruptJudgeThread extends Thread{
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    if (this.isInterrupted()){
                        System.out.println("已经是中断状态，即将退出");
                        return;//抛出异常也可以停止线程
                    }
                    timeCosumedOperation();
                    /**
                     * 测试时为了让这里执行地慢一点，使用了Thread.sleep(20)
                     * main线程中仅sleep 200毫秒，测试时发现总会抛出异常且中断线程失败
                     * 如果恰好在StopByExpThread sleep时 main线程进行了interrupt，会导致sleep中断异常
                     */
                    System.out.println("i = "+i);
                }
                System.out.println("run方法结尾");
            }
            public static void main(String[] args) {
                try{
                    StopByInterruptJudgeThread thread = new StopByInterruptJudgeThread();
                    thread.start();
                    Thread.sleep(800);
                    thread.interrupt();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        //1.7.4
        /**
         * 线程sleep状态下进行interrupt会抛出InterruptedException，并且清除停止状态值
         * 倒过来也会抛出同样的异常
         */
        //1.7.5
        /**
         * 使用stop方法的弊端
         * 强制停止线程可能会使一些清理工作得不到完成
         * 对一些锁定的对象进行了"解锁"，导致数据不一致
         */
        static class StopThread extends Thread{
            @Override
            public void run() {
                timeCosumedOperation();
            }

            public static void main(String[] args) {
                StopThread stopThread = new StopThread();
                stopThread.start();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopThread.stop();//会抛出ThreadDeath异常
            }
        }
        //1.7.7 stop造成数据不一致
        static class SyncObj{
            private String name = "a";
            private String pass = "b";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPass() {
                return pass;
            }

            public void setPass(String pass) {
                this.pass = pass;
            }
            public void setFields(String name, String pass){
                try {
                    this.name = name;
                    Thread.sleep(1000);
                    this.pass = pass;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        static class MyThread extends Thread{
            private SyncObj syncObj;
            @Override
            public void run() {
                syncObj.setFields("c","d");
            }
            public MyThread(SyncObj syncObj){
                this.syncObj = syncObj;
            }

            public static void main(String[] args) {
                SyncObj syncObj = new SyncObj();
                MyThread myThread = new MyThread(syncObj);
                myThread.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myThread.stop();
                System.out.println(syncObj.getName()+", "+syncObj.getPass());
            }
            /*
             打印出 c,b
             */
        }

        //1.7.8 使用return停止线程
        static class StopByReturnThread extends Thread{
            @Override
            public void run() {
                int i=0;
                while (true){
                    if (this.isInterrupted()){
                        System.out.println("线程停止");
                        return;
                    }
                    System.out.printf("thread executing times: %s\n",i++);
                }
            }
            public static void main(String[] args) throws InterruptedException {
                StopByReturnThread thread = new StopByReturnThread();
                thread.start();
                Thread.sleep(200);
                thread.interrupt();
            }
        }
        /**
         * 书中推荐使用抛出异常的方式停止线程
         * 在catch块中可以捕获异常，并将线程停止事件向上抛出，使线程停止事件得以传播
         */
        static class StopThreadByException1 extends Thread{
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    System.out.println("5秒后");
                    if (this.isInterrupted()){
                        System.out.println("通过抛出异常停止线程");
                        throw new InterruptedException();
                    }
                }catch (InterruptedException e){
                    System.out.println("线程停止了");
                    return;
                }
                System.out.println("为什还能继续执行？！");
            }
            public static void main(String[] args) {
                StopThreadByException1 thread = new StopThreadByException1();
                thread.start();
                twoSecondsWait();
                System.out.println("两秒后");
                thread.interrupt();
                /**
                 * 只打印出下面两行
                 * > 两秒后
                 * > 线程停止了
                 * 对于上述两行的解释是：interrupt睡着的线程一定会触发InterruptException
                 */
            }
        }
        static class StopThreadByException2 extends Thread{
            private void fiveSecondsWait(){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("发生异常");
                }
            }
            @Override
            public void run() {
                try {
                    fiveSecondsWait();
                    System.out.println("5秒后");
                    if (this.isInterrupted()){
                        System.out.println("通过抛出异常停止线程");
                        throw new InterruptedException();
                    }
                }catch (InterruptedException e){
                    System.out.println("线程停止了");
                    return;
                }
                System.out.println("为什还能继续执行？！");
            }
            public static void main(String[] args) {
                StopThreadByException2 thread = new StopThreadByException2();
                thread.start();
                twoSecondsWait();
                System.out.println("两秒后");
                thread.interrupt();
                /**
                 * > 两秒后
                 * > 发生异常
                 * > 5秒后
                 * > 为什还能继续执行？！
                 * 这个试验同时说明Thread.sleep要写在Thread类内
                 */
            }
        }
        static class StopThreadByException extends Thread{
            @Override
            public void run() {
                try {
                    fiveTimesOperation();//使用Thread.sleep实现的线程等待总是会因为interrupt而直接抛出异常跳出方法
                    if (this.isInterrupted()){
                        System.out.println("通过抛出异常停止线程");
                        throw new InterruptedException();
                    }
                }catch (InterruptedException e){
                    System.out.println("线程停止了");
                    return;
                }
                System.out.println("为什还能继续执行？！");
            }
            public static void main(String[] args) {
                StopThreadByException thread = new StopThreadByException();
                thread.start();
                twoSecondsWait();
                System.out.println("两秒后");
                thread.interrupt();
                /**
                 * > 两秒后
                 * > 通过抛出异常停止线程
                 * > 线程停止了
                 * 演示的目的达到了，while空转似乎太耗电了
                 */
            }
        }

        static class SuspendResumeThread extends Thread{
            @Override
            public void run() {
                super.run();
            }
        }
    }
}
