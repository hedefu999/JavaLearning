package com.ssmr.c10.springel.primary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

public class VariableParsing {
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class Role{
    private Integer id;
    private String name;
  }

  public static void main(String[] args) {
    Role role = new Role(12,"jack");

    ExpressionParser parser = new SpelExpressionParser();
    Expression expression = null;

    expression = parser.parseExpression("name");
    String name = (String) expression.getValue(role);
    System.out.println(name);//jack

    //变量环境类，将角色对象role作为其根节点
    EvaluationContext ctx = new StandardEvaluationContext(role);
//    expression = parser.parseExpression("name");
    expression.setValue(ctx,"lucy");
    name = expression.getValue(ctx,String.class);
    System.out.println(name);//lucy

    //调用方法
    expression = parser.parseExpression("getName()");
    name = expression.getValue(ctx,String.class);
    System.out.println(name);//lucy

    //新增环境变量
    List<String> list = new ArrayList<>();
    list.add("city");
    list.add("town");
    //给变量环境增加变量
    ctx.setVariable("list",list);
    //通过SPEL表达式读写环境变量
    expression = parser.parseExpression("#list[1]");
    expression.setValue(ctx,"province");
    name = expression.getValue(ctx,String.class);
    System.out.println(name);
    expression = parser.parseExpression("#list[0]");
    System.out.println(expression.getValue(ctx,String.class));//city

  }
}
