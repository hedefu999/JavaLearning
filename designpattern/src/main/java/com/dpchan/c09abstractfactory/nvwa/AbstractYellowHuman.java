package com.dpchan.c09abstractfactory.nvwa;

public abstract class AbstractYellowHuman implements Human {
    @Override
    public void getColor() {
        System.out.println("黄皮肤");
    }

    @Override
    public void talk() {
        System.out.println("你好");
    }
}
