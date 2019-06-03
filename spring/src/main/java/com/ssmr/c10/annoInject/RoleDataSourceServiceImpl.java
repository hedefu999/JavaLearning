package com.ssmr.c10.annoInject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoleDataSourceServiceImpl implements RoleDataService {
  @Autowired
  private DataSource dataSource;
  @Override
  public Role getRole(String name){
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    Role role = null;
    try {
      conn = dataSource.getConnection();
    }catch (Exception e){

    }
    try {
      dataSource.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
