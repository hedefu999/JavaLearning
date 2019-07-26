package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;

/**
 * 用于判断Role对象是否是null，决定是否打印
 */
public class RoleVerifierImpl implements RoleVerifier {
  @Override
  public boolean verify(Role role) {
    if (role == null){
      System.out.println("不可以打印");
      return false;
    }else {
      System.out.println("role实例不为空，可以打印");
      return true;
    }
  }
}
