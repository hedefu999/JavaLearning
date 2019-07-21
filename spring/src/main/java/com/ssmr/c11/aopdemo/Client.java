package com.ssmr.c11.aopdemo;

import com.ssmr.c11.Role;

public class Client {
  public static void main(String[] args) {
    RoleService roleService = new RoleServiceImpl();
    Interceptor interceptor = new RoleInterceptor();

    //通过getBean方法保存了被代理对象、拦截器和参数
    RoleService proxy = ProxyBeanFactory.getBean(roleService,interceptor);
    Role role = new Role();role.setId(12);role.setName("jack");
    System.out.println(proxy.printRole(role));

    System.out.printf("\n测试afterthrowing方法\n\n");
    proxy.printRole(null);

    //TODO @Transactional是如何获取方法执行结果并决定回滚的？
  }
}
