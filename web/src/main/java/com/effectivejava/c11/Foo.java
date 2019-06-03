package com.effectivejava.c11;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//不可序列化的有状态类 的 可序列化子类
public class Foo extends AbstractFoo implements Serializable {
  private static final long serialVersionUID = 265971303550678356L;

  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    //手动反序列化，初始化父类状态
    int x = ois.readInt();
    int y = ois.readInt();
    initialize(x,y);
  }
  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
    //手动序列化父类状态
    oos.writeInt(getX());
    oos.writeInt(getY());
  }
  public Foo(int x,int y){super(x,y);}

}
