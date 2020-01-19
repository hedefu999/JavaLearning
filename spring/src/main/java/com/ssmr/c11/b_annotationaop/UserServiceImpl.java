package com.ssmr.c11.b_annotationaop;

import com.ssmr.c11.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service("userService")
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RoleService roleService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public String getRoleParseResult(){
        log.info("UserService位于的线程名：{}", Thread.currentThread().getName());
        log.info("UserService位于的事务名：{}", TransactionSynchronizationManager.getCurrentTransactionName());
        Role role = new Role();role.setId(13);role.setName("lucy");
        String result = roleService.processParam(role, true);
        log.info("上层服务拿到返回结果：result = {}", result);
        return result;
    }
}
