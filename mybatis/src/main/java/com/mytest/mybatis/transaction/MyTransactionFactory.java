package com.mytest.mybatis.transaction;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public class MyTransactionFactory implements TransactionFactory {
    private Logger log = LoggerFactory.getLogger("MyTransactionFactory");
    public void setProperties(Properties props) {
        log.info("setting properties");
    }

    public Transaction newTransaction(Connection conn) {
        log.info("create transaction with one conn");
        return new MyTransaction(conn);
    }

    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        log.info("create transaction with full params");
        return new MyTransaction(dataSource,level,autoCommit);
    }
}
