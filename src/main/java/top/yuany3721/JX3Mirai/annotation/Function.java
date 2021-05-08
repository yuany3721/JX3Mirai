package top.yuany3721.JX3Mirai.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Function {
    // 功能名
    String name() default "";

    // 功能用法
    String usage() default "";

    // 是否默认关闭
    boolean close() default false;
}
