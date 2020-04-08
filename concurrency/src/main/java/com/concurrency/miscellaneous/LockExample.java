package com.concurrency.miscellaneous;

public class LockExample {
    public static void main(String[] args) {
        synchronized (LockExample.class) {
            System.out.println("lock");
        }
    }
}
