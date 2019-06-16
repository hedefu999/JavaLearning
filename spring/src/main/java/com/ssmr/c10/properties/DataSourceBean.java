package com.ssmr.c10.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 在Bean中引用读入的Property配置
 */
@Data
@Component
public class DataSourceBean {
  @Value("${db.username}")
  private String userName = null;
  @Value("${db.url}")
  private String url = null;
}
