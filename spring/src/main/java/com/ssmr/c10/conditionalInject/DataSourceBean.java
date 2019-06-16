package com.ssmr.c10.conditionalInject;

import lombok.Data;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import javax.sql.DataSource;
import java.util.Properties;

@Data
public class DataSourceBean {
  @Value("${db.driver}")
  private String driver = null;

  @Value("${db.url}")
  private String url = null;

  @Value("${db.username}")
  private String username = null;

  @Value("${db.password}")
  private String password = null;

  @Bean(name = "dataSource")
  @Conditional(DataSourceCondition.class)
//依据实现了org.springframework.context.annotation.Condition接口的类DataSourceCondition中的matches方法返回的结果确定是否生成Bean
  public DataSource getDataSource(@Value("${db.driver}") String driver,
                                  @Value("${db.url}") String url,
                                  @Value("${db.username}") String username,
                                  @Value("${db.password}") String password){
    Properties props = new Properties();
    props.setProperty("driver", driver);
    props.setProperty("url", url);
    props.setProperty("username", username);
    props.setProperty("password", password);
    DataSource dataSource = null;
    try {
      dataSource = BasicDataSourceFactory.createDataSource(props);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return dataSource;
  }
}
