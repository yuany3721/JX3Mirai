package top.yuany3721.JX3Mirai.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Buf {
    // 缓存名 无缓存名则表示不需要读写缓存文件
    String bufName() default "";
}
