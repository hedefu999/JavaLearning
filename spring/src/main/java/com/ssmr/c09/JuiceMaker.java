package com.ssmr.c09;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Data
public class JuiceMaker{
    private Logger log = LoggerFactory.getLogger("JuiceMaker");

    private String shopName;
    private JuiceSource juiceSource;


}
