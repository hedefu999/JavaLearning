package com.ssmr.c10.properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = ("classpath:jdbc.properties"),ignoreResourceNotFound = true)
public class ApplicationConfig {
}
