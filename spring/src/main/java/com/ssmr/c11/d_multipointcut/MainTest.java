package com.ssmr.c11.d_multipointcut;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTest {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MultiAopConfig.class);
        MultiAspects mu = context.getBean(MultiAspects.class);
        mu.testMultiAspects();
        //2 1 3的执行顺序是按照切面上的@Order注解来的，1 2 3的次序
        //如果没有@Order注解，按么此处before的执行顺序是 1 3 2，是按照MultiAopConfig中的bean生成顺序来的
        /**
         * before 2 ...
         * before 1 ...
         * before 3 ...
         * testMultiAspects...
         * after 3 ...
         * after 1 ...
         * after 2 ...
         */
        //spring底层是通过责任链来处理多切面的调用的
    }
}
