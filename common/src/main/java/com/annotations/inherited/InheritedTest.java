package com.annotations.inherited;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Inherited: 使用此注解声明出来的自定义注解，表示将来使用InheritedTest的类及其子类会自动继承此注解
 * .@Inherited只能用于类，对方法属性无效
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InheritedTest {
    String value();
}
