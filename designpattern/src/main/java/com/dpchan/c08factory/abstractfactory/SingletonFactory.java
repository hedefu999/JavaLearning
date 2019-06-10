package com.dpchan.c08factory.abstractfactory;

import java.lang.reflect.Constructor;

public class SingletonFactory {
    class Singleton{}//外部类
    private static Singleton singleton;
    static {
        try {
            Class clazz = Class.forName(Singleton.class.getName());
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            singleton = (Singleton) constructor.newInstance();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static Singleton getSingleton(){
        return singleton;
    }
}
