package com.effectivejava.c11.readobject;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;
//极力确保自身及内部域不可变的类
public final class Period implements Serializable {
  private final Date start;
  private final Date end;
  public Period(Date start,Date end){
    this.start = new Date(start.getTime());
    this.end = new Date(end.getTime());
    if (this.start.compareTo(this.end) > 0){
      throw new IllegalArgumentException("日期倒置");
    }
  }
  //使用保护性拷贝
  public Date getStart(){return new Date(start.getTime());}
  public Date getEnd(){return new Date(end.getTime());}
  public String toString(){return start+" - "+end;}

  /**
   * 问题在于，简单地implements Serializable并不能确保安全性，手工修改类序列化后的字节码（如置换start end）会破坏对象
   * 所以需要提供一个readObject方法检查并阻止反序列化过程
   * @param ois
   */
  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    if (this.start.compareTo(this.end) > 0){
      throw new IllegalArgumentException("日期倒置");
    }
  }

  /**
   * 当一个对象被反序列化的时候，对于客户端不应该拥有的对象引用，如果哪个域包含了这样的对象引用，就必须要做保护性拷贝
   * readObject2方法要求去掉start end的final修饰
   * * @param ois
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void readObject2(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
//    start = new Date(start.getTime());
//    end = new Date(end.getTime());
    if (start.compareTo(end) > 0)
      throw new InvalidObjectException(start+" after "+end);
  }
}
