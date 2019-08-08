package com.ssmr.mine.exceptionaop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.ssmr.mine.exceptionaop")
public class AopConfig {
    @Bean
    public ExceptionAspect exceptionAspect(){
        return new ExceptionAspect();
    }
}
