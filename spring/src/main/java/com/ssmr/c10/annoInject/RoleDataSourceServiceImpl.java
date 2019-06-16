package com.ssmr.c10.annoInject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component("roleDataService")
public class RoleDataSourceServiceImpl implements RoleDataService {
  @Autowired
  private DataSource dataSource;
  @Override
  public Role getRole(String name){
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    Role role = new Role();
    try {
      String sql = "select * from `role` where role_name = ?";
      conn = dataSource.getConnection();
      ps = conn.prepareStatement(sql);
      ps.setString(1,name);
      ResultSet resultSet = ps.executeQuery();
      while (resultSet.next()){
        role.setId(resultSet.getInt("id"));
        role.setRoleName(resultSet.getString("role_name"));
        role.setNote(resultSet.getString("note"));
      }
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    try {
      if (ps != null){
        ps.close();
      }
      if (rs!= null) rs.close();
      if (conn != null) conn.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return role;
  }
}
