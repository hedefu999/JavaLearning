package com.annotations.inherited;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestInheritedAnno {
    private static Logger log = LoggerFactory.getLogger("TestInheritedAnno");
    public static void main(String[] args) {
        Class<Child> child = Child.class;
        log.info("=========打印子类上所有类上的注解");
        log.info("类名：{}.",child.getSimpleName());
        for(Annotation annotation : child.getAnnotations()){
            log.info(JSON.toJSONString(annotation));
        }
        log.info("=========打印子类上所有方法的注解");
        for(Method method : child.getMethods()){
            log.info("方法名：{}.", method.getName());
            for(Annotation annotation : method.getAnnotations()){
                log.info(JSON.toJSONString(annotation));
            }
        }
        log.info("=========打印子类所有属性上的注解");
        for(Field field : child.getFields()){
            log.info("字段名：{}.",field.getName());
            for(Annotation annotation : field.getAnnotations()){
                log.info(JSON.toJSONString(annotation));
            }
        }
        //日志结果：
        /**
         * =========打印子类上所有类上的注解
         * 类名：Child.
         * {"value":"使用Inherited的注解 class"}
         * =========打印子类上所有方法的注解
         * 方法名：method.
         * 方法名：method2.
         * {"value":"使用Inherited的注解 method2"}
         * {"value":"未使用Inherited的注解 method2"}
         * 方法名：wait.
         * 方法名：wait.
         * 方法名：wait.
         * 方法名：equals.
         * 方法名：toString.
         * 方法名：hashCode.
         * 方法名：getClass.
         * 方法名：notify.
         * 方法名：notifyAll.
         * =========打印子类所有属性上的注解
         * 字段名：a.
         * {"value":"使用Inherited的注解 field"}
         * {"value":"未使用Inherited的注解 field"}
         */
        /**
         * 总结：
         * 子类可以得到父类的Inherited类型的类注解，子类的覆写方法拿不到父类方法上的Interited类型注解
         * Inherited类型注解用在方法和字段上达不到继承的效果，子类只能直接访问父类的方法和字段才能获得注解
         * Inherited类型注解用在类上可以将注解传递给子类
         */
    }
}
