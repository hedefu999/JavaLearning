package com.redpacket.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;

/**
 * 无配置文件的web项目，使用了AbstractAnnotationConfigDispatcherServletInitializer可以而且必需删除dispatcher-servlet.xml和web.xml两个文件
 * WebApplicationInitializer接口实现类
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    //配置SpringIoC容器的上下文
    @Override
    protected Class<?>[] getRootConfigClasses() {
        logger.info("进入getRootConfigClasses方法");
        return new Class<?>[]{RootConfig.class};
    }
    //DispatcherServlet环境配置
    @Override
    protected Class<?>[] getServletConfigClasses() {
        logger.info("进入getServletConfigClasses方法");
        return new Class<?>[]{WebConfig.class};
    }
    //DispatcherServlet拦截请求配置,请求链接不需要.do,这个.do导致404了好长时间
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    /**
     * dynamic servlet上传文件配置,需要覆写父类的方法
     */
    @Override
    protected void customizeRegistration(javax.servlet.ServletRegistration.Dynamic registration) {
        String filepath = "/Users/hedefu/Documents/DEVELOPER/IDEA/JavaLearning/redpacket/src/main/resources/upload";
        //计算5-10MB的字节数
        long singleMax = (long) (5*Math.pow(2,20));
        long totalMax = (long) (10*Math.pow(2,20));
        registration.setMultipartConfig(new MultipartConfigElement(filepath,singleMax,totalMax,0));
        //super.customizeRegistration(registration);
    }
}
