package com.dpchan.c12proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class GamePlayerIH implements InvocationHandler {
    Class clazz = null;
    Object object = null;
    public GamePlayerIH(Object o){
        this.object = o;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //before method was invoked
        Object result = method.invoke(this.object,args);
        //after method was invoked
        return result;
    }
}
