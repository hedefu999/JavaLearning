package com.redpacket.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.*;

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
    //配置成仅处理/redpacket/开头的请求，便于favicon.ico请求通过
    @Override
    protected String[] getServletMappings() {
        /**
         * 实际测试中 /redpacket/* 不能匹配/redpacket/grab,原因不明，可以使用/解决问题，但favicon.ico总是请求不到
         * 另外/*不能匹配任何请求，似乎*不能使用
         * 如果有xml配置文件可以在dispatcher-servlet中加一行 <mvc:resources mapping="/resources/**" location="/resources/"/>
         * TODO 强制在此处手动加入URL才能访问
         */
        String[] urls = {
                "/index/main",
                "/redpacket/grab","/redpacket/grab-red-packet","/redpacket/grab-red-packet_with-redis","/redpacket/test-return","/redpacket/grab-red-packet_retry-fixed-times","/redpacket/grab-red-packet_retry-fixed-duration","/redpacket/grab-red-packet_with-version",
                "/exportExcel",
                "/file/upload","/file/uploadMultipart","/file/uploadPart",
                "/getHeaderAndCookie","/testInterceptor",
                "/advice/test","/advice/exception","/advice/getInitUser",
        };
        String[] url = {"/"};
        return urls;
    }

    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        ServletRegistration servletRegistration =
                servletContext.getServletRegistration(AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME);
        super.registerDispatcherServlet(servletContext);
    }

    @Override
    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        return super.registerServletFilter(servletContext, filter);
    }

    /**
     * dispatcher servlet上传文件配置,需要覆写父类的方法
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
