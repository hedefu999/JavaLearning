package com.mytest.mybatis.typeHandler;

import com.mytest.mybatis.enums.SexEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SexEnumTypeHandler extends BaseTypeHandler<SexEnum>{
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, SexEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i,parameter.getCode());
    }
    @Override
    public SexEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Integer code = rs.getInt(columnName);
        return SexEnum.getByCode(code);
    }
    @Override
    public SexEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Integer code = rs.getInt(columnIndex);
        return SexEnum.getByCode(code);
    }
    @Override
    public SexEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Integer code = cs.getInt(columnIndex);
        return SexEnum.getByCode(code);
    }
}
