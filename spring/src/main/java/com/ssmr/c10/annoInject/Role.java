package com.ssmr.c10.annoInject;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Data
@Component
public class Role {
  @Value("12")
  private Integer code;
  @Value("technician")
  private String name;
}
