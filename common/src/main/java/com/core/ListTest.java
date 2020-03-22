package com.core;

import org.junit.Test;

import java.util.*;

public class ListTest {
    @Test
    public void test5(){
        List<String> list = new ArrayList<>();
        list.add("jacxk");
        Collections.synchronizedList(list);
    }

    public static void main(String[] args) throws Exception{
        final HashMap<String, String> map = new HashMap<String, String>(2);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(), "");
                        }
                    }, "moon" + i).start();
                }
            }
        }, "ftf");
        t.start();
        t.join();

    }
}
