package com.ssmr.c10.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

@Component
public class ProfileDataSource {
  @Bean("devDataSource")
  @Profile("dev")
  public DataSource getDevDataSource(){
    Properties props = new Properties();
    props.setProperty("","");
    return null;
  }
  @Bean("testDataSource")
  @Profile("test")
  public DataSource getTestDataSource(){
    return null;
  }
}
