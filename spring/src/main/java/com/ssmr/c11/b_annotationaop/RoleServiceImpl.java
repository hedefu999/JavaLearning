package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class RoleServiceImpl implements RoleService {
  private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);
  @Override
  public void printRole(Role role) {
    System.out.println(role.getId()+role.getName());
  }

  @Override
  public void printRole2(Role role) {
    System.out.printf("编号：%s，名字：%s。", role.getId(), role.getName());
  }

  @Override
  public void printRole3(Role role, Integer code) {
    System.out.printf("入参role是：%s，code = %d%n", role.toString(), code);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
  public String processParam(Role role, boolean addDesc){
      log.info("RoleService位于的线程名：{}", Thread.currentThread().getName());
      log.info("RoleService位于的事务名：{}", TransactionSynchronizationManager.getCurrentTransactionName());
      log.info("解析参数：role， addDesc");
      if (addDesc){
          return "这是角色："+role.toString();
      }else {
          return role.toString();
      }
  }
}
