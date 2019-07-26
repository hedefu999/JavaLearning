package com.dpchan.c12proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client {
    public static void main(String[] args) {
        IGamePlayer player = new GamePlayer("张三");
        //定义一个hanlder
        InvocationHandler handler = new GamePlayerIH(player);
        //开始打游戏，记下时间戳
        System.out.println("开始时间是：2009-8-25 10:45");
        //获得类的class loader
        ClassLoader cl = player.getClass().getClassLoader();
        //动态产生一个代理者
        IGamePlayer proxy = (IGamePlayer) Proxy.newProxyInstance(cl,new Class[]{IGamePlayer.class},handler);
        //登录
        proxy.login("zhangSan", "password");
        //开始杀怪
        proxy.killBoss();
        //升级
        proxy.upgrade();
    }
}
