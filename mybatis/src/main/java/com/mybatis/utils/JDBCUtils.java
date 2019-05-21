package com.mybatis.utils;

import com.mysql.jdbc.Driver;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JDBCUtils {
    /**
     * 一个似乎通用的JDBC查询工具
     * 使用Map组织入参不通，因为Map不保证遍历时的顺序与加入时一致
     * @param jdbcConfigPath
     * @param sql
     * @param params
     * @return
     */
    public static ResultSet executeQuery(String jdbcConfigPath, String sql, Object... params){
        Properties properties = PropertiesUtils.readPropsFile(jdbcConfigPath);
        if (properties == null || properties.size() == 0){
            throw new RuntimeException("属性文件读取失败");
        }
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String passsword = properties.getProperty("db.password");
        ResultSet resultSet = null;
        try{
            Driver driver = new Driver();
            DriverManager.registerDriver(driver);
            Connection conn = DriverManager.getConnection(url,user,passsword);
            PreparedStatement ps = conn.prepareStatement(sql);
            int index = 1;
            for (Object value : params){
                if (String.class.isInstance(value)){
                    ps.setString(index++, (String) value);
                }else if (Integer.class.isInstance(value)){
                    ps.setInt(index++, (Integer) value);
                }else if (Double.class.isInstance(value)){
                    ps.setDouble(index++, (Double) value);
                }
            }
            resultSet = ps.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }
        //进一步完善应当把model的字段类型传入，以便resultSet转为对应的对象
        //主要是为了将resultSet关闭掉
        //set.close();
        //ps.close();
        //conn.close();
        return resultSet;
    }
}