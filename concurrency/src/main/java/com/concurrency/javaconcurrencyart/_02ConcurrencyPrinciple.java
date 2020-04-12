package com.concurrency.javaconcurrencyart;

import com.concurrency.JOLUtils;
import org.openjdk.jol.info.ClassLayout;
import sun.management.counter.Counter;

import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 并发的底层原理
 */
public class _02ConcurrencyPrinciple {
    /**
     * jol展示jvm中object header特性
     * refto https://blog.csdn.net/baidu_28523317/article/details/104453927
     */
    static class JavaObjectLayout {
        static class Student {
            private String name;
            private Integer age;
            private Boolean married;

            public Student(String name, Integer age, boolean sex) {
                this.name = name;
                this.age = age;
                this.married = sex;
            }
        }
        static class Student2 {
            private String name;
            private int age;
            private boolean married;

            public Student2(String name, Integer age, boolean sex) {
                this.name = name;
                this.age = age;
                this.married = sex;
            }
        }

        public static void main(String[] args) {
            Student stu1 = new Student("jack is a good man",12,false);
            JOLUtils.printObjectHeader(stu1);
            System.out.println(Integer.toBinaryString(stu1.hashCode()));
            System.out.println(Integer.toHexString(stu1.hashCode()));

            Student2 stu2 = new Student2("name",12,false);
            JOLUtils.printObjectHeader(stu2);


            Object plainObject = new Object();
            JOLUtils.printObjectHeader(plainObject);
            System.out.println(Integer.toHexString(plainObject.hashCode()));
        }

    }
    /**
     * MarkWord在四种锁状态下的内容变化
     */
    static class MarkWordInFourLevel{
        /**
         * 无锁状态下的跳过，0000 0001
         */

        /**
         * 偏向锁情况下，偏向锁是延迟加载的，所有对象初始是无锁状态，延时后进入可偏向状态
         */
        static class LockObj { }
        static class BiasedLockLevel0{
            public static void main(String[] args) throws InterruptedException {
                LockObj a = new LockObj();
                JOLUtils.printObjectHeader(a);
            }
            /*
             * 无锁
             * 00000001 00000000 00000000 00000000
             * 00000000 00000000 00000000 00000000
             * 10000001 11000010 00000000 11111000
             *
             * 如果 VM Options 添加 -XX:BiasedLockingStartupDelay=0 : 偏向锁
             * 00000101 00000000 00000000 00000000
             * 00000000 00000000 00000000 00000000
             * 10000001 11000010 00000000 11111000
             */
        }
        static class BiasedLockLevel0Wait{
            public static void main(String[] args) throws InterruptedException {
                Thread.sleep(5000);
                LockObj a = new LockObj();
                JOLUtils.printObjectHeader(a);
                //System.out.println(ClassLayout.parseInstance(a).toPrintable());
            }
            /*
             * 偏向锁 0000 0101
             * 00000101 00000000 00000000 00000000
             * 00000000 00000000 00000000 00000000
             * 01000011 11000010 00000000 11111000
             *
             * 如果VM Options配置 -XX:-UseBiasedLocking : 无锁 00000001
             * 00000001 00000000 00000000 00000000
             * 00000000 00000000 00000000 00000000
             * 01000011 11000010 00000000 11111000
             */
        }
        /**
         * 上述两种偏向锁的ThreadID和epoch（共56位）都是0，此时并没有偏向任何线程，仅表明锁对象处于可偏向的状态
         * 进入sync同步代码块后才写入偏向线程ID
         */
        static class BiasedLockLevel{
            public static void main(String[] args) throws InterruptedException {
                Thread.sleep(5000);
                LockObj a = new LockObj();
                synchronized (a){
                    JOLUtils.printObjectHeader(a);

                    System.out.println(Thread.currentThread().getId());
                    System.out.println(Long.toBinaryString(Thread.currentThread().getId()));
                }
            }
            /*
             * 偏向锁 00000101，当前只有一个线程竞争锁
             * 00000101 00101000 10000000 00010100
             * 10100101 01111111 00000000 00000000
             * 00000101 11010110 00000000 11111000
             *
             * 如果VM Options配置 -XX:-UseBiasedLocking : 轻量级锁 11110000
             * 11110000 00001000 10101101 00001100
             * 00000000 01110000 00000000 00000000
             * 00000101 11010110 00000000 11111000
             */
        }
        /**
         * 踩坑：hashCode方法会导致偏向锁无法进入！因为31位被占用拿来填充hashCode了，偏向锁的ThreadID没地方放了
         * 无法进入偏向锁，此时进入的是轻量级锁
         */
        static class BiasedLockLevel2{
            public static void main(String[] args) throws InterruptedException {
                Thread.sleep(5000);
                Object lock = new Object();
                //System.identityHashCode(lock); //A          100111101111000010011000010110
                System.out.println(Integer.toBinaryString(lock.hashCode())); //B 100111101111000010011000010110
                System.out.println(Integer.toHexString(lock.hashCode()));
                JOLUtils.printObjectHeader(lock);
                synchronized (lock){
                    JOLUtils.printObjectHeader(lock);
                }
            }/*
             * 偏向锁
             * 00000101 00000000 00000000 00000000
             * 00000000 00000000 00000000 00000000
             * 11100101 00000001 00000000 11111000
             * 偏向锁
             * 00000101 01010000 00000000 01001011
             * 10000111 01111111 00000000 00000000
             * 11100101 00000001 00000000 11111000
             *
             * 打开上述代码中的A或B行
             * 无锁
             * 00000001 00010110 00100110 10111100
             * 00100111 00000000 00000000 00000000
             * 11100101 00000001 00000000 11111000
             *
             * 轻量级锁
             * 11110000 10101000 11010000 00000011
             * 00000000 01110000 00000000 00000000
             * 11100101 00000001 00000000 11111000
             */
        }

        //在偏向锁，轻量锁，重量锁，hashcode会被转移到Monitor中。
        /**
         * =-=-=-=-=-=- 下面是竞争环境下锁的情况 -=-=-=-=-=-=-=
         * 轻量级锁情况下
         */
        static class LightWeightLockLevel{
            public static void main(String[] args) throws InterruptedException {
                Thread.sleep(5000);
                Object lock = new Object();
                Thread thread1 = new Thread(){
                    @Override
                    public void run() {
                        synchronized (lock){
                            JOLUtils.printObjectHeader(lock);//A 00000101
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) { }
                        }
                    }
                };
                Thread thread2 = new Thread(){
                    @Override
                    public void run() {
                        synchronized (lock){
                            JOLUtils.printObjectHeader(lock);//B 11101010
                        }
                    }
                };
                thread1.start();
                Thread.sleep(1000);//C
                thread2.start();
            }
            /*
             * A处是偏向锁(101)，B处是轻量级锁(00)，偏向锁出现非偏向线程竞争，膨胀成轻量级锁
             * C处时间改为2000，A处是偏向锁101，B处是重量级锁10
             */
        }
        static class LightWeightLockLevel2{
            public static void main(String[] args) throws InterruptedException {
                Thread.sleep(5000);
                Object lock = new Object();
                Thread thread1 = new Thread(){
                    @Override
                    public void run() {
                        synchronized (lock){
                            JOLUtils.printObjectHeader(lock);//A
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) { }
                    }
                };
                Thread thread2 = new Thread(){
                    @Override
                    public void run() {
                        synchronized (lock){
                            JOLUtils.printObjectHeader(lock);//B
                        }
                    }
                };
                thread1.start();
                Thread.sleep(1000);//C
                thread2.start();
            }
            /*
             * 将thread1同步代码块执行时间缩短，sleep移出sync代码块，让锁处于无竞争状态下足够长的时间
             * A处是偏向锁101，B处是轻量级锁00
             */
        }
        /**
         * 直接竞争
         */
        static class WightLock{
            public static void main(String[] args) throws InterruptedException {
                Thread.sleep(5000);
                Object lock = new Object();
                Thread thread1 = new Thread(){
                    @Override
                    public void run() {
                        synchronized (lock){
                            JOLUtils.printObjectHeader(lock);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) { }
                        }
                        JOLUtils.printObjectHeader(lock);
                    }
                };
                Thread thread2 = new Thread(){
                    @Override
                    public void run() {
                        synchronized (lock){
                            JOLUtils.printObjectHeader(lock);
                            //try {
                            //    Thread.sleep(2000);
                            //} catch (InterruptedException e) { }
                        }
                        JOLUtils.printObjectHeader(lock);
                    }
                };
                thread1.start();
                thread2.start();
                Thread.sleep(3000);
                JOLUtils.printObjectHeader(lock);//A
            }
            /**
             * A处是无锁的状态01，其他所有JOLUtils调用处都是重量级锁10
             */
        }

    }

    static class CASSimulation{
        private AtomicInteger atomicI = new AtomicInteger(0);
        private int i = 0;
        public static void main(String[] args) {
            final CASSimulation cas = new CASSimulation();
            List<Thread> ts = new ArrayList<Thread>(600);
            long start = System.currentTimeMillis();
            for (int j = 0; j < 100; j++) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10000; i++) {
                            cas.count();
                            cas.safeCount();
                        }
                    }
                });
                ts.add(t);
            }
            for (Thread t : ts) {
                t.start();
            }
            // 等待所有线程执行完成
            for (Thread t : ts) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(cas.i);//996414
            System.out.println(cas.atomicI.get());//100 0000
            System.out.println(System.currentTimeMillis() - start);
        }
        /**        * 使用CAS实现线程安全计数器        */
        private void safeCount() {
            for (;;) {
                int i = atomicI.get();
                boolean suc = atomicI.compareAndSet(i, ++i);
                if (suc) {
                    break;
                }
            }
        }
        /**
         * 非线程安全计数器
         */
        private void count() {
            i++;
        }
    }

}
