package com.brick.core.bean;

import com.brick.core.bean.annotation.Bean;
import com.brick.core.bean.impl.DefaultClassScanner;
import com.brick.core.exception.InitializationError;
import com.brick.web.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author maigeiye
 * @Description 实现了容器接口的默认Bean容器类
 * @version 1.0
 **/
public class ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    /**
     * Bean的核心容器，key为Bean的Class对象，value为Bean的实例对象
     */
    private static final Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();

    static {
        try {
            logger.info("容器初始化");
            ClassScanner classScanner = new DefaultClassScanner();
            List<Class<?>> classList = classScanner.getClassList();
            for (Class<?> clazz : classList) {
                if (clazz.isAnnotationPresent(Bean.class) || clazz.isAnnotationPresent(Controller.class)) {
                    // 将有Bean注解的类实例化
                    Object beanInstance = clazz.newInstance();
                    // 把创建的实例放入BeanMap中
                    beanMap.put(clazz, beanInstance);
                }
            }
            // 把ClassScanner对象注入容器，供后续使用
            beanMap.put(ClassScanner.class, classScanner);
            logger.info("容器创建成功");
        } catch (Exception e) {
            throw new InitializationError("初始化ApplicationContext错误");
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        if (!beanMap.containsKey(clazz)) {
            throw new RuntimeException("容器中没有此实例");
        }
        return (T) beanMap.get(clazz);
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return beanMap;
    }
}
