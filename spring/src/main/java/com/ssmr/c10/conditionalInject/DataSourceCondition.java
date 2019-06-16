package com.ssmr.c10.conditionalInject;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
//@Conditional(XXX.class)注解可以指定springgramework根据条件规则决定是否创建Bean
public class DataSourceCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    //通过ConditionContext可以获得Spring的运行环境，AnnotatedTypeMetadata可以提供关于Bean的注解信息
    Environment env = context.getEnvironment();
    //判断是否存在完整的数据源配置
    return env.containsProperty("jdbc.database.driver")
      && env.containsProperty("jdbc.database.url")
      && env.containsProperty("db.username")
      && env.containsProperty("db.password");
  }
}
