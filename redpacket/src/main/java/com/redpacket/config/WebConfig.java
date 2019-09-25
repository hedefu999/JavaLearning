package com.redpacket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(value = "com.redpacket",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Controller.class)})
@EnableWebMvc
public class WebConfig {//extends WebMvcConfigurerAdapter
    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    //初始化RequestMappingHandlerAdapter，并加载Http的json转换器
    //使用MappingJackson2HttpMessageConverter接收JSON类型消息
    @Bean
    public HandlerAdapter handlerAdapter(){
        //使用RequestMappingHandlerAdapter使其能够支持JSON格式 @RequestBody的转换
        RequestMappingHandlerAdapter requestAdapter = new RequestMappingHandlerAdapter();
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(mediaType);
        jsonConverter.setSupportedMediaTypes(mediaTypes);
        //控制器在遇到注解@RequestBody时就知道采用JSON类型进行应答
        requestAdapter.getMessageConverters().add(jsonConverter);
        return requestAdapter;
    }
}
