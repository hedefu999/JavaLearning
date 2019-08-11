package com.ssmr.c12;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/ssmr/c12/simple-driver-data-source.xml")
public class OperateDBWithJDBCTemplate {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    public void testJDBCTemplateQuery() {
        //ApplicationContext context = new ClassPathXmlApplicationContext("com/ssmr/c12/simple-driver-data-source.xml");
        //JdbcTemplate jdbcTemplate = context.getBean(JdbcTemplate.class);
        int id = 1;
        String sql = "select id,`name`,age,note from role where id = ?";
        Object[] sqlArgs = new Object[]{id};
        //argsType这个int数组的填写见java.sql.Types
        Role role = jdbcTemplate.queryForObject(sql, sqlArgs, new int[]{4}, new RowMapper<Role>() {
            @Override
            public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                System.out.println("row num = "+rowNum);//0
                Role result = new Role();
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt(3));
                result.setNote(rs.getString(4));
                return result;
            }
        });
        log.info("查询结果：{}.",JSON.toJSONString(role));
    }

    @Test
    public void testJDBCTemplateListQuery(){
        String sql = "select id,`name`,age from role where id < 3";
        List<Role> roles = jdbcTemplate.query(sql, new RowMapper<Role>() {
            @Override
            public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
                log.info("传入的rowNum = {}", rowNum);
                Role result = new Role();
                result.setId(rs.getInt(1));
                result.setName(rs.getString(2));
                result.setAge(rs.getInt(3));
                //result.setNote(rs.getString(4)); 这行要注掉
                return result;
            }
        });
        log.info("查询结果：{}.", JSON.toJSONString(roles));
        /**
         *  传入的rowNum = 0
         *  传入的rowNum = 1
         *  查询结果：[{"age":20,"id":1,"name":"teacher"},{"age":25,"id":2,"name":"solicitor"}].
         */
    }

    @Test
    public void testInsert(){
        Role result = new Role();
        result.setName("developer");
        result.setAge(5);
        result.setNote("god does coding as well");
        String sql = "insert into role(name,age,note)values(?,?,?)";
        int operateRows = jdbcTemplate.update(sql,result.getName(),result.getAge(),result.getNote());
        log.info("受影响的行数：{}.",operateRows);
        //delete sql用的也是update
    }
}
