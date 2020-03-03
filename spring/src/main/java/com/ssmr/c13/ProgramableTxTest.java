package com.ssmr.c13;

import com.ssmr.c13.xmltx.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/ssmr/c13/spring-mybatis.xml")
public class ProgramableTxTest {
    /**
     * 编程式的事务管理，极少使用
     * jdbcTemplate本身的数据库事务交由默认存在的PlatformTransactionManager管理
     */
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PlatformTransactionManager txManager;
    @Test
    public void test(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(definition);

        String sql = "insert into role(name, age, note) VALUE(?,?,?)";
        try{
            jdbcTemplate.update(sql,"geensden",12,"test transaction again");
            Thread.sleep(3000);
            int i = 1/0;
            txManager.commit(status);//提交事务
        }catch(Exception e){
            txManager.rollback(status);
        }
    }
    //TODO 玩注解 注解的 别名 @Documented什么意思
    /**
     * 声明式事务
     * 当你的业务方法不发生异常，或者发生异常，但该异常也被配置为允许提交事务，则让事务管理器回滚事务
     */
    //@Transactional(...)

    /**
     * XML配置的事务管理器
     */
    @Autowired
    private RoleService roleServiceImpl;
    @Autowired
    private RoleService roleServiceAdapter;
    @Test
    public void testXMLTxManager(){
        try {
            System.out.println(roleServiceImpl.saveRole());
        }catch (Exception e){
            System.out.println(e.getCause());
        }
        try {
            System.out.println(roleServiceAdapter.saveRole());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    //TODO Spring最后执行的一条jdbc的rollback方法在哪里？

}
