package com.ssmr.c11.a_aopprimary;

public class ProxyBeanFactory {
  /**
   * 一个对象通过ProxyBeanFactory.getBean方法定义后，具有下述特征
   * > Bean必须是一个实现了某一个接口的对象，执行流程：
   *  * ----------------> before ---> bean方法
   *                                     |
   *                         after-------
   *                           ↓
   *  afterThrowing <--是-- 产生异常 --否--> afterReturning
   *        |                                   |
   *         -------------->结 束<---------------
   * > 最先会执行拦截器的before方法
   * > 其次执行Bean的方法，通过反射的形式
   * > 执行Bean方法时，无论是否产生异常，都会执行after方法
   * > 执行Bean方法时，如果不产生异常，则执行afterReturning方法，如果发生异常，则执行afterThrowing方法
   * （上述约定也是SpringAOP的约定）
   * @param obj  传入接口的实现类可以返回interface代理
   * @param interceptor
   * @param <T>
   * @return
   */
  public static <T> T getBean(T obj, Interceptor interceptor){
    return (T) ProxyBeanUtil2.getBean(obj,interceptor);
  }
}
