package com.brick.web;

import com.brick.core.bean.ApplicationContext;
import com.brick.core.bean.ClassScanner;
import com.brick.web.annotation.Controller;
import com.brick.web.annotation.RequestMapping;
import com.brick.web.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author maigeiye
 * @Description 处理器映射器
 * @version 1.0
 **/
public class HandlerMapping {
    private Map<String, Handler> map = new ConcurrentHashMap<String, Handler>();

    public HandlerMapping() {
        putHandler();
    }

    /**
     * 将Handler放入映射容器
     */
    private void putHandler() {
        ClassScanner classScanner = ApplicationContext.getBean(ClassScanner.class);
        List<Class<?>> classList = classScanner.getClassList();
        for (Class<?> clazz : classList) {
            if (clazz.isAnnotationPresent(Controller.class)) {
                // 获取并遍历Controller中所有方法
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        // 获取方法的参数名字和参数类型
                        Map<String, Class<?>> params = new LinkedHashMap<>();
                        for (Parameter methodParam : method.getParameters()) {
                            RequestParam requestParam = methodParam.getAnnotation(RequestParam.class);
                            if (requestParam == null) {
                                throw new RuntimeException("必须要有RequestParam指定的参数");
                            }
                            params.put(requestParam.value(), methodParam.getType());
                        }
                        // 获取方法上的RequestMapping注解的路径
                        String path = method.getAnnotation(RequestMapping.class).path();
                        if (map.containsKey(path)) {
                            throw new RuntimeException("url重复注册:" + path);
                        }
                        // 生成Handle
                        Handler handler = new Handler(clazz, method, params);
                        map.put(path, handler);
                    }
                }
            }
        }
    }


    public Handler getHandler(String requestPath) {
        return map.get(requestPath);
    }
}
