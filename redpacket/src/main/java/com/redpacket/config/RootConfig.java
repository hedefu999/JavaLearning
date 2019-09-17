package com.redpacket.config;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
//使用配置有Filters的ComponentScan. 为了便于在Junit中的测试Mapper，这里要保证RootConfig只扫描必需的bean
//非Web的配置扫描到WebAppInitializer会导致Failed to load ApplicationContext(No ServletContext set),junit无法提供web环境
@ComponentScan(
        value = "com.redpacket.*",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION,value = {Controller.class, RestController.class}),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = {WebAppInitializer.class, WebConfig.class})
        })
@EnableTransactionManagement
@EnableAsync
public class RootConfig {
    @Bean("dataSource")
    public static DataSource dataSource(){
        Properties props = new Properties();
        props.setProperty("driverClassName","com.mysql.jdbc.Driver");
        props.setProperty("url","jdbc:mysql://localhost:3306/ssmr?useSSL=false");
        props.setProperty("username","root");
        props.setProperty("password","hedefu999");
        DataSource dataSource = null;
        try {
            dataSource = BasicDataSourceFactory.createDataSource(props);
        }catch (Exception e){
            e.printStackTrace();
        }
        return dataSource;
    }
    @Bean("sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(@Autowired DataSource dataSource)throws Exception{
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        ///Users/hedefu/Documents/DEVELOPER/IDEA/JavaLearning/redpacket/src/main/resources/mybatis-config.xml
        Resource resource = new ClassPathResource("mybatis-config.xml");
        sqlSessionFactory.setConfigLocation(resource);
        return sqlSessionFactory;
    }
    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.redpacket.repository");
        msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
        msc.setAnnotationClass(Repository.class);
        return msc;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    //配置spring线程池
    @Bean("taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("redpacket-taskExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
