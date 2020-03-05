package com.redpacket.config;

import com.redpacket._16.String2UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@ComponentScan(value = "com.redpacket",
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Controller.class)})
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
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
        //注册MappingJackson2HttpMessageConverter到HandlerAdapter
        //这样@ResponseBody就能找到正确的HttpMessageConverter
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(mediaType);
        jsonConverter.setSupportedMediaTypes(mediaTypes);
        //控制器在遇到注解@RequestBody时就知道采用JSON类型进行应答
        requestAdapter.getMessageConverters().add(jsonConverter);
        return requestAdapter;
    }//上面bean的注册方式对应xml
    /*
    * <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    *     <property name="messageConverters">
    *         <list>
    *             <ref bean="jsonConverter"/>
    *         </list>
    *     </property>
    * </bean>
    * <bean id="jsonConverter" class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
    *     <property name="supportedMediaTypes">
    *         <list>
    *             <value>application/json;charset=UTF-8</value>
    *         </list>
    *     </property>
    * </bean>
    * */


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



    /*-=-=-=-=-=-=-=- i18n =-=-=-=-=-=-=-=-=-=-*/
    //i18n需要的bean,名称必须是 messageSource
    @Bean(name = "messageSource")
    public MessageSource initMessageSource(){
        ResourceBundleMessageSource msgSrc = new ResourceBundleMessageSource();
        msgSrc.setDefaultEncoding("UTF-8");
        //setBasename方法传递的是一个classpath路径下的文件名
        msgSrc.setBasename("i18n/msg");
        return msgSrc;
    }
    //@Bean(name = "messageSource")
    public MessageSource initReloadableMessageSource(){
        ReloadableResourceBundleMessageSource msgSrc =
                new ReloadableResourceBundleMessageSource();
        msgSrc.setDefaultEncoding("UTF-8");
        msgSrc.setBasename("classpath:msg");
        //msgSrc.setBasenames();
        //缓存3600秒，然后重新加载文件
        msgSrc.setCacheSeconds(3600);//-1 永不刷新
        return msgSrc;
    }
    /**
     * 上面两种对应的xml配置方式
     * <bean id = "messageSource" class="org.springframework.context.support.ResourceBundleMessageSource>
     *  <property name="defaultEncoding" value="UTF-8"/>
     *  <property name="basenames" value="msg"/>
     * </bean>
     *
     * <bean id="messageSource" class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
     *     <property name="defaultEncoding" value="UTF-8"/>
     *     <property name="basenames" value="classpath:msg"/>
     *     <property name="CacheSeconds" value="3600"/>
     * </bean>
     */

    //@Bean("localeResolver")//约定bean名称，不可修改
    public LocaleResolver initCookieLocaleResolver(){
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("lang");
        localeResolver.setCookieMaxAge(180);
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
    }
    @Bean("localeResolver")//约定bean名称，不可修改
    public LocaleResolver initSessionLocaleResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
        /**
         * SessionLocaleResolver定义了两个静态公共常量
         * LOCALE_SESSION_ATTRIBUTE_NAME、TIME_ZONE_SESSION_ATTRIBUTE_NAME
         */
    }
    @Bean("localeChangeInterceptor")
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInter = new LocaleChangeInterceptor();
        localeChangeInter.setParamName("language");
        return localeChangeInter;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        super.addInterceptors(registry);
    }

    //书中说 使用注解@EnableWebMVC或在xml中使用<mvc:annotation-driven/>时系统会自动初始化FormattingConversionServiceFactoryBean实例
    //未能注入，无法使用转换工具
    //@Autowired
    //private FormattingConversionServiceFactoryBean fcsfb;
    //private List<Converter> specialDataConverter = null;
    //@Bean(name = "specialUserDataConverter")
    //public List<Converter> initUserConverter(){
    //    if (specialDataConverter == null){
    //        specialDataConverter = new ArrayList<>();
    //    }
    //    Converter userConverter = new String2UserConverter();
    //    specialDataConverter.add(userConverter);
    //
    //    fcsfb.getObject().addConverter(userConverter);
    //    return specialDataConverter;
    //}
}
