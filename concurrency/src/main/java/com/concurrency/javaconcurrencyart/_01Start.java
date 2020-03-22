package com.concurrency.javaconcurrencyart;

public class _01Start {
    static class ConcurrencyTest {
        private static final long count = 10000l;
        public static void main(String[] args) throws InterruptedException {
            concurrency();
            serial();
        }
        private static void concurrency() throws InterruptedException {
            long start = System.nanoTime();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int a = 0;
                    for (long i = 0; i < count; i++) {
                        a += 5;
                    }
                }
            });
            thread.start();
            int b = 0;
            for (long i = 0; i < count; i++) {
                b--;
            }
            long time = System.nanoTime() - start;
            thread.join();
            System.out.println("concurrency :" + time+"ns,b="+b);
        }
        private static void serial() {
            long start = System.nanoTime();
            int a = 0;
            for (long i = 0; i < count; i++) {
                a += 5;
            }
            int b = 0;
            for (long i = 0; i < count; i++) {
                b--;
            }
            long time = System.nanoTime() - start;
            System.out.println("serial:" + time+"ns,b="+b+",a="+a);
        }
    }
}
