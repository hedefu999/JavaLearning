package com.mybatis.chapter08;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({
        @Signature(
            type = StatementHandler.class,
            method = "prepare",
            args = {Connection.class,Integer.class}
        )
})//拦截器签名表示此处拦截的是StatementHandler接口的prepare方法
public class SimplePlugin implements Interceptor{
    private Logger log = LoggerFactory.getLogger("MyPlugin");
    private Properties props = null;

    //代替StatementHandler的prepare方法
    //返回预编译后的PreparedStatement
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        Object object = null;
        //分离代理对象链（由于目标类可能被多个拦截器插件拦截，从而形成多次代理，通过循环可以分离出最原始的目标类）
        while (metaStatementHandler.hasGetter("h")){
            object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }
        statementHandler = (StatementHandler) object;
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        Object parameterObject = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
        log.info("执行的SQL：【{}】",sql);
        log.info("参数：【{}】", JSON.toJSONString(parameterObject, SerializerFeature.WriteNullStringAsEmpty));
        log.info("before ...");
        //如果当前代理的是一个非代理对象，那么它就会调用真实拦截对象的方法，如果不是，就会调度下一个插件代理对象的invoke方法
        Object obj = invocation.proceed();
        log.info("after ...");
        return obj;
    }

    //生成代理对象
    @Override
    public Object plugin(Object target) {
        //采用系统默认的Plugin.wrap方法生成代理对象
        return Plugin.wrap(target,this);
    }

    //MyBatis初始化时，就会生成插件实例，并且调用这个方法,这个方法的调用时机比较早
    //Property属性的传入可以在mybatis-config.xml中配置
    @Override
    public void setProperties(Properties properties) {
        this.props = properties;
        log.info("dbType = {}.",this.props.get("dbType"));
    }
}
