package com.mybatis.utils;

import com.hedefu.mybatis.model.Role;
import com.hedefu.mybatis.mapper.RoleMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.IOException;
import java.io.InputStream;
//加锁保证SqlSessionFactory创建的唯一性
public class SqlSessionFactoryUtils {
    private final static Class<SqlSessionFactoryUtils> LOCK = SqlSessionFactoryUtils.class;
    private static SqlSessionFactory sqlSessionFactory = null;
    private SqlSessionFactoryUtils(){}

    public static SqlSessionFactory getSqlSessionFactory(String configFileRPath){
        synchronized (LOCK){
            if (sqlSessionFactory != null){
                return sqlSessionFactory;
            }
            //String configFile = "mybatis-config.xml";
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream(configFileRPath);
                SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
                sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return sqlSessionFactory;
        }
    }

    //使用代码方式生成SqlSessionFactory
    public static SqlSessionFactory getSqlSessionFactory2(){
        synchronized (LOCK){
            PooledDataSource dataSource = new PooledDataSource();
            dataSource.setDriver("com.mysql.jdbc.Driver");
            dataSource.setUsername("root");
            dataSource.setPassword("hedefu999");
            dataSource.setUrl("jdbc:mysql://localhost:3306/ssmr");
            dataSource.setDefaultAutoCommit(false);
            TransactionFactory transacFact = new JdbcTransactionFactory();
            Environment env = new Environment("dev", transacFact, dataSource);
            Configuration config = new Configuration(env);
            //注册一个mybatis上下文别名
            config.getTypeAliasRegistry().registerAlias("role",Role.class);
            config.addMapper(RoleMapper.class);
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = builder.build(config);
            return sqlSessionFactory;
        }
    }

    public static SqlSession openSqlSession(String configFileRPath){
        if (sqlSessionFactory == null){
            getSqlSessionFactory(configFileRPath);
        }
        return sqlSessionFactory.openSession();
    }

    public static <T> T getMapper(Class<T> clazz, String configFileRPath){
        SqlSessionFactory sessFact = getSqlSessionFactory(configFileRPath);
        SqlSession sqlSession = sessFact.openSession();
        T t = sqlSession.getMapper(clazz);
        return t;
    }

}
