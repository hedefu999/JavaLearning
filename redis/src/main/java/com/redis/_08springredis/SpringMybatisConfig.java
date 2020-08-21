package com.redis._08springredis;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.redis._08springredis")
/**
 * 书上实现TransactionManagementConfigurer接口的配置类写法会扫不进com.redis.springredis08下的Service，折腾半天还得用自己写过的好用
 */
public class SpringMybatisConfig {
    @Bean("dataSource")
    public static DataSource dataSource(){
        Properties props = new Properties();
        props.setProperty("driverClassName","com.mysql.cj.jdbc.Driver");
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
        //Resource resource = new ClassPathResource("mybatis/mybatis-config.xml");
        //sqlSessionFactory.setConfigLocation(resource);
        //完全抛弃mybatis配置文件的写法，似乎繁琐地很
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setCacheEnabled(true);
        config.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
        config.setLogPrefix("redisLearing - ");
        config.setCallSettersOnNulls(true);
        sqlSessionFactory.setConfiguration(config);
        Resource resource = new FileUrlResource("src/main/resources/08springredis/UserMapper.xml");
        Resource[] resources = new Resource[]{resource};
        sqlSessionFactory.setMapperLocations(resources);
        return sqlSessionFactory;
    }
    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.redis._08springredis");
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


}
