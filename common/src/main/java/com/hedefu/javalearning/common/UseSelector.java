package com.hedefu.javalearning.common;

public class UseSelector {
    public static void main(String[] args) {
        Sequence sequence = new Sequence(10);
        for (int i = 0; i < 10; i++) {
            sequence.add("数字："+i);
        }
        Selector selector = sequence.selector();
        while (!selector.end()){
            System.out.println(selector.current());
            selector.next();
        }
    }
}
