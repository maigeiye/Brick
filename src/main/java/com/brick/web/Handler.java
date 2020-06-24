package com.brick.web;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author maigeiye
 * @Description 封装Controller相关信息
 * @version 1.0
 **/
public class Handler {

    private Class<?> controller;
    private Method method;
    private Map<String, Class<?>> methodParameter;

    public Handler(Class<?> controller, Method method, Map<String, Class<?>> methodParameter) {
        this.controller = controller;
        this.method = method;
        this.methodParameter = methodParameter;
    }

    public Class<?> getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    public Map<String, Class<?>> getMethodParameter() {
        return methodParameter;
    }
}
