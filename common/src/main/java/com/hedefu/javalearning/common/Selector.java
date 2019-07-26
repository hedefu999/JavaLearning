package com.hedefu.javalearning.common;

public interface Selector {
    boolean end();//判断是否到达结尾处
    Object current();
    void next();
}
