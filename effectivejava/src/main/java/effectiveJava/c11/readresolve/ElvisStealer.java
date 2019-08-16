package effectiveJava.c11.readresolve;

import java.io.Serializable;

public class ElvisStealer implements Serializable {
  private static final long serialVersionUID = -5938864733677153285L;
  static Elvis impersonator;
  private Elvis payload;
  private Object readResolve(){
    //存储未掉调用过readResolve方法的Elvis实例的引用
    impersonator = payload;
    //返回favorite字段的正确类型
    return new String[]{"A fool such as I"};
  }
}
