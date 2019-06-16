package com.ssmr.c10.springel.primary;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class First {
  public static void main(String[] args) {
    ExpressionParser parser = new SpelExpressionParser();
    Expression expression = null;

    expression = parser.parseExpression("'hello world'");//单引号不可省略
    String str = (String) expression.getValue();
    System.out.println(str);

    //通过EL访问普通方法
    expression = parser.parseExpression("'hello world'.charAt(0)");
    char character = (char) expression.getValue();
    System.out.println(character);

    //通过EL访问getter方法
    expression = parser.parseExpression("'hello world'.bytes");
    byte[] bytes = (byte[]) expression.getValue();
    System.out.println(bytes);

    //通过EL访问属性
    expression = parser.parseExpression("'hello world'.bytes.length");
    int length = (int) expression.getValue();
    System.out.println(length);
    //可以不必知道返回值的类型
    Class clazz = expression.getValueType();
    System.out.println(clazz.cast(expression.getValue()));

    //获取生成的对象
    expression = parser.parseExpression("new String('abcd')");
    String abc = (String) expression.getValue();
    System.out.println(abc);
    System.out.println(expression.getExpressionString());
    System.out.println(expression.getValueType());

  }
}
