package com.atguigu.starter.cache.annotation;
/**
 * 缓存注解
 */
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface GmallCache {
    String cacheKey() default "";//就是cachekey
    String bloomName() default "";//如果指定了布隆过滤器名字，就用
    String bloomValue() default "";//指定布隆过滤器如果需要判定的话，用什么表达式计算出的值进行判定
    String lockName() default "";//传入精确锁就用精确的，否则用全局默认的
    long ttl() default 60*30L;
}
