package com.brick.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author maigeiye
 * @Description 请求的方法参数名
 * @version 1.0
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * 方法参数别名
     */
    String value() default "";

    /**
     * 参数是否必传
     */
    boolean required() default true;
}

