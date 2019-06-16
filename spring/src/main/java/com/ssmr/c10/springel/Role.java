package com.ssmr.c10.springel;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 使用SpEL设置值
 */
@Data
@Component("role")
public class Role {
  @Value("#{1}")
  private Integer id;
  @Value("#{'teacher'}")
  private String roleName;
  @Value("#{'work in school'}")
  private String note;
}
