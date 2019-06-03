package com.effectivejava.c11.readobject;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;

public class SerializationProxy<E extends Enum<E>> implements Serializable {
  private static final long serialVersionUID = 1416441325739064672L;
  private final Class<E> elementType;
  private final Enum[] elements;

//  private final Date start;
//  private final Date end;

  SerializationProxy(EnumSet<E> set){
    elementType = null;//set.elementType; ??
    elements = null;//set.toArray(EMPTY_ENUM_ARRAY);
  }

  private Object readResolve(){
    EnumSet<E> result = EnumSet.noneOf(elementType);
    for (Enum e : elements){
      result.add((E) e);
    }
    return result;
  }
  //序列化代理模式的writeReplace方法
  //writeReplace方法在序列化之前，将外围类的实例转变成了它的序列化代理
//  private Object writeReplace(){
//    return new SerializationProxy(this);
//  }
}
