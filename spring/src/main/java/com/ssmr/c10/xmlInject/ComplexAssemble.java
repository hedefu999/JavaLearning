package com.ssmr.c10.xmlInject;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Data
public class ComplexAssemble {
  private String name;
  private List<String> list;
  private Map<Integer,String> map;
  private Properties props;
  private Set<String> set;
  private String[] array;
}
