package com.ssmr.c09;

import org.springframework.beans.factory.DisposableBean;

/**
 * 从打印出的日志可见，DisposableBean接口针对的是SpringIoC容器，在容器关闭后才会执行
 * DisposableBean接口中的destroy在Bean自定义的destroy方法之后执行
 */
public class DisposableBeanImpl implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean的destroy方法");
    }
}
