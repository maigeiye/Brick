package com.brick.core.bean;

import com.brick.core.bean.annotation.Autowired;
import com.brick.core.exception.InitializationError;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Author maigeiye
 * @Description 依赖注入功能实现类
 * @version 1.0
 **/
public class DependencyInjection {
    static {
        try {
            Map<Class<?>, Object> beanMap = ApplicationContext.getBeanMap();
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 获取Bean的Class对象和实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取Bean中的所有字段
                Field[] fields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(fields)) {
                    for (Field field : fields) {
                        // 找到带有Autowired注解的字段执行注入操作
                        if (field.isAnnotationPresent(Autowired.class)) {
                            // 获取字段的Class对象
                            Class<?> clazz = field.getType();
                            if (clazz.isInterface()) {
                                // 获取字段对应的实现类
                                Class<?> implClass = getImpl(clazz);
                                // 获取注入对象
                                Object injectInstance = beanMap.get(implClass);
                                // 暴力注入
                                field.setAccessible(true);
                                field.set(beanInstance, injectInstance);
                            } else {
                                Object injectInstance = beanMap.get(clazz);
                                field.setAccessible(true);
                                field.set(beanInstance, injectInstance);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new InitializationError("依赖注入失败", e);
        }
    }

    /**
     * 获取接口的实现类
     */
    public static Class<?> getImpl(Class<?> superClass) {
        ClassScanner classScanner = ApplicationContext.getBean(ClassScanner.class);
        Class<?> implClass = classScanner.getClassBySuper(superClass);
        if (implClass != null) {
             return implClass;
        } else {
            throw new InitializationError("注入字段为没有实现类的接口");
        }
    }
}
