package com.effectivejava.c11.primary;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubSimpleEmployee extends SimpleEmployee {
  private String address;

//  public SubSimpleEmployee(String name, Integer age, String address) {
//    super(name, age);
//    this.address = address;
//  }
}
