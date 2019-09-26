package com.redpacket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;
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

    //multipartResolver是spring的约定bean名称，不可修改
    @Bean("multipartResolver")
    public MultipartResolver initMultipartResolver(){
        return new StandardServletMultipartResolver();
    }

    //使用需要依赖apache commons-fileupload的方式，兼容3.0以下低版本的servlet
    //@Bean("multipartResolver")
    public MultipartResolver initCommonsMultipartResolver(){
        String filepath = "";
        long singleMax = (long) (5*Math.pow(2,20));
        long totalMax = (long) (10*Math.pow(2,20));
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(singleMax);
        resolver.setMaxUploadSize(totalMax);
        try {
            resolver.setUploadTempDir(new FileSystemResource(filepath));
        } catch (IOException e) {
        }
        return resolver;
    }
}
