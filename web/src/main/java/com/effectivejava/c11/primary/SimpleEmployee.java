package com.effectivejava.c11.primary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor //实现Serializable接口的类的子类也是具备序列化特性的，但父类里必须声明一个默认的无参构造函数
public class SimpleEmployee implements Serializable {
  private static final long serialVersionUID = -3187784505501326206L;
  //加transient以声明字段不参与序列化，static变量也是不参与序列化的
  private String name;
  private Integer age;
}
