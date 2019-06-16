package com.ssmr.c10.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.ssmr.c10.properties"})
@PropertySource(value = {"classpath:jdbc.properties"},ignoreResourceNotFound = true)
public class ApplicationConfig2 {
  /**
   * PropertySourcesPlaceholderConfigurer可以解析属性占位符
   * @return
   */
  @Bean
  public PropertySourcesPlaceholderConfigurer propSrcPlaceHolderConfig(){
    return new PropertySourcesPlaceholderConfigurer();
  }
}
