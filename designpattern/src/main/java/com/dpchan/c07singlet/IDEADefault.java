package com.dpchan.c07singlet;

/**
 * IDEA生成的单例模式
 * 默认是恶汉模式，不存在线程安全问题
 */
public class IDEADefault {
    private static IDEADefault ourInstance = new IDEADefault();

    public static IDEADefault getInstance() {
        return ourInstance;
    }

    private IDEADefault() {
    }
}
