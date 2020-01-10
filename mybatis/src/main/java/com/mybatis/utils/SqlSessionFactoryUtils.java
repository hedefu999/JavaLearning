package com.mybatis.utils;

import com.mytest.mybatis.model.Role;
import com.mytest.mybatis.mapper.RoleMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//加锁保证SqlSessionFactory创建的唯一性
public class SqlSessionFactoryUtils {
    private final static Class<SqlSessionFactoryUtils> LOCK = SqlSessionFactoryUtils.class;
    private static SqlSessionFactory sqlSessionFactory = null;
    private SqlSessionFactoryUtils(){}

    //使用property stream生成SqlSessionFactory
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
    public static SqlSessionFactory getSqlSessionFactory2(String jdbcFileRPath){
        synchronized (LOCK){
            Properties props = null;
            try {
                props = Resources.getResourceAsProperties(jdbcFileRPath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            PooledDataSource dataSource = new PooledDataSource();
            dataSource.setDriver("com.mysql.jdbc.Driver");
            dataSource.setUsername(props.getProperty("db.username"));
            dataSource.setPassword(props.getProperty("db.password"));
            dataSource.setUrl(props.getProperty("db.url"));
            dataSource.setDefaultAutoCommit(true);
            dataSource.setDefaultTransactionIsolationLevel(TransactionIsolationLevel.READ_COMMITTED.getLevel());

            TransactionFactory transacFact = new JdbcTransactionFactory();
            Environment env = new Environment("dev", transacFact, dataSource);
            Configuration config = new Configuration(env);

            //单个注册和批量注册 别名+Mapper
            //config.getTypeAliasRegistry().registerAlias("role",Role.class);
            //config.addMapper(RoleMapper.class);
            config.getTypeAliasRegistry().registerAliases("com.mybatis.chapter05.model");
            config.addMappers("com.mybatis.chapter05.mapper");

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

    public static <T> T getMapper2(Class<T> clazz, String jdbcFileRPath){
        SqlSessionFactory sqlSessFact = getSqlSessionFactory2(jdbcFileRPath);
        SqlSession sqlSession = sqlSessFact.openSession();
        T mapper = sqlSession.getMapper(clazz);
        return mapper;
    }

    public static <T> T getMapper(Class<T> clazz, String configFileRPath){
        SqlSessionFactory sessFact = getSqlSessionFactory(configFileRPath);
        //SqlSession sqlSession = sessFact.openSession();
        //这里可以调整autoCommit或事务隔离级别，但限制较多，建议在DataSource中设置好
        SqlSession sqlSession = sessFact.openSession(true);
        //SqlSession sqlSession1 = sessFact.openSession(TransactionIsolationLevel.READ_COMMITTED);
        T t = sqlSession.getMapper(clazz);
        //sqlSession.close();
        return t;
    }

}
