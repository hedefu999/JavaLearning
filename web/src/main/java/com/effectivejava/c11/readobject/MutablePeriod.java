package com.effectivejava.c11.readobject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class MutablePeriod {
  final Period period;
  final Date start;
  final Date end;
  MutablePeriod()throws Exception{
    //构造函数的这种写法可以添加final域的引用,从而可以在main方法中篡改
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(new Period(new Date(),new Date()));
    //append rogue "previous object refs" for internal Date fields in Period
    //详见"Java Object Serialization Specificaton" Section 6,4
    byte[] ref = {0x71,0,0x7e,0,5};
    baos.write(ref);
    ref[4] = 4;
    baos.write(ref);
    ObjectInputStream ois = new ObjectInputStream(
      new ByteArrayInputStream(baos.toByteArray())
    );
    period = (Period) ois.readObject();
    start = (Date) ois.readObject();
    end = (Date) ois.readObject();

//    start = new Date();end = new Date();
//    period = new Period(start, end);
  }
  public static void main(String[] args) throws Exception{
    MutablePeriod mp = new MutablePeriod();
    Period period = mp.period;
    Date pEnd = mp.end;
    pEnd.setYear(78);
    System.out.println(period);
    pEnd.setYear(69);
    System.out.println(period);
  }
}
