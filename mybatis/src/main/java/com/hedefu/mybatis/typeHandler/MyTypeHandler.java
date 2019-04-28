package com.hedefu.mybatis.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyTypeHandler extends BaseTypeHandler<String> {
    private Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        //preparedStatement见JDBC，i表示参数的位置，parameter就是JavaType传入的参数
        log.info("为preparedStatement设置第{}个jdbcType为{}的参数{}.", i, jdbcType.name(),parameter);
        ps.setString(i,parameter);
    }
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        log.info("从结果集ResultSet中获取名为{}的结果。", columnName);
        String result = rs.getString(columnName);
        log.info("结果是{}.", result);
        return result;
    }
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        log.info("从结果集ResultSet中获取第{}栏的结果。", columnIndex);
        String result = rs.getString(columnIndex);
        log.info("结果是{}.", result);
        return result;
    }
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return result;
    }
}
