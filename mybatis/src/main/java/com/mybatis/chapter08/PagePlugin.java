package com.mybatis.chapter08;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class,Integer.class}
        )
})// TODO 拦截StatementHandler的其他方法试试
public class PagePlugin implements Interceptor {
    private Logger log = LoggerFactory.getLogger("PagePlugin");
    private static Integer DEFAULT_PAGE_SIZE = 10;
    private static Integer DEFAULT_PAGE_INDEX = 1;
    private static Boolean DEFAULT_ACTIVE_STATUS = false;
    private static Boolean DEFAULT_CHECK_PAGE_INDEX = false;
    private static Boolean DEFAULT_CLEAN_ORDERBY = false;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //只要符合@Intercepts签名的规则，StatementHandler在运行时就会进到这里
        StatementHandler stmtHandler = (StatementHandler) getUnproxyObject(invocation.getTarget());
        MetaObject metaStatementHandler = SystemMetaObject.forObject(stmtHandler);
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        if (!checkIsSelectSQL(sql)){
            log.info("拦截到的SQL语句不是select查询语句");
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
        Object paramObject = boundSql.getParameterObject();
        PageParam pageParam = getPageParamsFromParamObject(paramObject);
        if (pageParam == null){
            log.info("未传入分页参数，将使用配置的分页参数");
            return invocation.proceed();
        }
        Boolean activeStatus = pageParam.getUseFlag() == null?this.DEFAULT_ACTIVE_STATUS:pageParam.getUseFlag();
        if (!activeStatus){
            return invocation.proceed();
        }
        Integer pageNo = pageParam.getPageIndex()==null?DEFAULT_PAGE_INDEX:pageParam.getPageIndex();
        Integer pageSize = pageParam.getPageSize()==null?DEFAULT_PAGE_SIZE:pageParam.getPageSize();
        Boolean checkPageIndex = pageParam.getCheckFlag()==null?DEFAULT_CHECK_PAGE_INDEX:pageParam.getCheckFlag();
        Boolean checkCleanOrderBy = pageParam.getCleanOrderBy()==null?DEFAULT_CLEAN_ORDERBY:pageParam.getCleanOrderBy();
        //计算总条数
        int total = getTotalCount(invocation,metaStatementHandler,boundSql,checkCleanOrderBy);
        //回填总条数到分页参数
        pageParam.setTotal(total);
        //计算总页数
        int totalPage = total%pageSize==0?total/pageSize:total/pageSize+1;
        //回填分页总数
        pageParam.setTotalPage(totalPage);
        //检查当前页码有效性
        checkPageNo(checkPageIndex,pageNo,totalPage);
        //修改SQL
        return prepareSQL(invocation, metaStatementHandler, boundSql, pageNo, pageSize);
    }
    //处理添加了分页查询的SQL
    private Object prepareSQL(Invocation invocation, MetaObject metaStatementHandler, BoundSql boundSql, Integer pageNo, Integer pageSize) {
        String sql = boundSql.getSql();
        String newSql= "select * from ("+sql+") $_mybatisPluginTmpTable limit ?,?";
        //修改当前需要执行的SQL
        metaStatementHandler.setValue("delegate.boundSql.sql",newSql);
        Object stmt = null;
        try {
            //
            PreparedStatement ps = (PreparedStatement) invocation.proceed();
            //设置分页参数
            this.preparePageDataParams(ps, pageNo, pageSize);
            return ps;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //设置分页参数
    private void preparePageDataParams(PreparedStatement ps, Integer pageNo, Integer pageSize) {
        try {
            int paramCount = ps.getParameterMetaData().getParameterCount();
            log.info("参数的数量应当等于2：{}.", paramCount == 2);
            ps.setInt(paramCount - 1,(pageNo - 1)*pageSize);
            ps.setInt(paramCount,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPageNo(Boolean checkPageIndex, Integer pageNo, int totalPage) {
        log.info("检查页码： pageNo = {}, totalPage = {}.", pageNo, totalPage);
        if (checkPageIndex){
            if (pageNo > totalPage){
                throw new RuntimeException("分页参数错误：超出最大页数");
            }
        }
    }

    private int getTotalCount(Invocation invocation, MetaObject metaStatementHandler, BoundSql boundSql, Boolean checkCleanOrderBy) {
        MappedStatement mappedStmt = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        Configuration config = mappedStmt.getConfiguration();
        String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        if (checkCleanOrderBy){
            sql = this.cleanOrderByForSQL(sql);
        }
        //改写为查询总数的SQL
        String countSql = "select count(*) as total from ("+sql+") $_mybatisPluginTmpTable";
        //TODO 参数第一个是Connection？
        Connection conn = (Connection) invocation.getArgs()[0];
        PreparedStatement ps = null;
        int total = 0;
        try {
            ps = conn.prepareStatement(countSql);
            //修改BoundSql中的sql至查询总数的SQL
            BoundSql countBoundSql = new BoundSql(config,countSql,boundSql.getParameterMappings(),boundSql.getParameterObject());
            //构建mybatis的ParamHandler来设置countSql的参数
            ParameterHandler handler = new DefaultParameterHandler(mappedStmt,boundSql.getParameterObject(),countBoundSql);
            //向拦截到的原SQL中设置参数，使用原本就存在的参数映射数据
            //使用in ('','')形式的SQL无法进行分页，报错。某些查询SQL不能使用分页插件
            //Parameter '__frch_name_0' not found. Available parameters are [names, pageParam, param1, param2]
            handler.setParameters(ps);
            //执行查询
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                total = resultSet.getInt("total");
            }
        } catch (Exception e) {
            log.info("发生异常：{}。", e.getMessage());
            e.printStackTrace();
        } finally {
            //此处关闭Connection会导致后续SQL无法继续
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("查询到的记录总数是{}。", total);
        return total;
    }
    //查询总数时不需要排序，去掉以提升性能
    private String cleanOrderByForSQL(String sql) {
        int index = sql.indexOf("order by");
        return index == -1 ?sql:sql.substring(0,index);
    }

    private PageParam getPageParamsFromParamObject(Object paramObject) {
        PageParam pageParam = null;
        if (paramObject != null){
            if (paramObject instanceof Map){
                @SuppressWarnings("unchecked")
                Map<String, Object> paramMap = (Map<String, Object>) paramObject;
                for(Map.Entry<String,Object> entry : paramMap.entrySet()){
                    Object value = entry.getValue();
                    if (value instanceof PageParam){
                        return (PageParam) value;
                    }
                }
            }else
            //参数是或者继承PageParam
            if (paramObject instanceof PageParam){
                return (PageParam) paramObject;
            }else {
            //从POJO属性尝试读取分页参数
                Field[] fields = paramObject.getClass().getDeclaredFields();
                //尝试从POJO中获得类型为PageParam的属性
                for (Field field : fields){
                    if (field.getType() == PageParam.class){
                        try {
                            //java自带的反射获取Field的Getter/Setter方法！
                            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), paramObject.getClass());
                            Method method = pd.getReadMethod();
                            return (PageParam) method.invoke(paramObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return pageParam;
    }

    private boolean checkIsSelectSQL(String sql) {
        String trimSql = sql.trim();
        return trimSql.startsWith("select");
    }

    //从责任链中分离出最原始的StatementHandler对象
    private Object getUnproxyObject(Object target){
        MetaObject metaStatementHandler = SystemMetaObject.forObject(target);
        //分离代理对象链（目标类可能被多个拦截器拦截，从而形成多级代理，通过循环可以分离出最原始的目标类）
        Object object = null;
        //TODO 这个h啥意思？
        while (metaStatementHandler.hasGetter("h")){
            object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }
        if (object == null){
            return target;
        }
        return object;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }
    //为插件设置默认值
    @Override
    public void setProperties(Properties properties) {
        DEFAULT_PAGE_SIZE = Integer.parseInt(properties.getProperty("default.pageSize","10"));
        DEFAULT_PAGE_INDEX = Integer.parseInt(properties.getProperty("default.pageIndex","1"));
        DEFAULT_ACTIVE_STATUS = Boolean.parseBoolean(properties.getProperty("default.activeStatus","false"));
        DEFAULT_CHECK_PAGE_INDEX = Boolean.parseBoolean(properties.getProperty("default.checkPageIndex","false"));
        DEFAULT_CLEAN_ORDERBY = Boolean.parseBoolean(properties.getProperty("default.cleanOrderby","false"));

    }
}
