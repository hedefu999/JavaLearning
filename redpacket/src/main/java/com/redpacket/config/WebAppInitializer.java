package com.redpacket.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }
    //DispatcherServlet环境配置
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }
    //DispatcherServlet拦截请求配置
    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.do"};
    }
    /**
     * dynamic servlet上传文件配置
     */
    @Override
    protected void customizeRegistration(javax.servlet.ServletRegistration.Dynamic registration) {
        String filepath = "/Users/hedefu/Documents/DEVELOPER/IDEA/JavaLearning/redpacket/src/main/resources/upload";

        super.customizeRegistration(registration);
    }
}
