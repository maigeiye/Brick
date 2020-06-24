package com.brick.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author maigeiye
 * @Description 请求绑定
 * @version 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    /**
     * 请求路径
     */
     String path() default "";

    /**
     * 请求方法
     */
    RequestMethod method() default RequestMethod.GET;
}
