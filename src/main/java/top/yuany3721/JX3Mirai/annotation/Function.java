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

    // 是否对用户可见
    boolean visible() default true;

    // 是否需要管理员权限（发送者）
    boolean needAdmin() default false;

    // 是否需要管理员权限（机器人）
    boolean needBotAdmin() default false;
}
