package com.ssmr.c11.d_multipointcut;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.ssmr.c11.d_multipointcut")
public class MultiAopConfig {
    @Bean
    public Aspect1 aspect1(){
        return new Aspect1();
    }
    @Bean
    public Aspect3 aspect3(){
        return new Aspect3();
    }
    @Bean
    public Aspect2 aspect2(){
        return new Aspect2();
    }

}
