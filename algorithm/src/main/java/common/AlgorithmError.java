package common;

import java.lang.annotation.*;

/**
 * 发现一个有趣的现象：注解加在java下会无法使用，所以所有的类还是要有个包名的
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
@Documented
public @interface AlgorithmError {
    String reason() default "";
}
