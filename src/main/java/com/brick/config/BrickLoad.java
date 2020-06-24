package com.brick.config;

import com.brick.core.bean.ApplicationContext;
import com.brick.core.bean.DependencyInjection;
import com.brick.core.exception.InitializationError;
import com.brick.jdbc.DataAccessor;
import com.brick.jdbc.impl.DefaultDataAccessor;

/**
 * @Author maigeiye
 * @Description 加载框架类
 * @version 1.0
 **/
public class BrickLoad {

    /**
     * 初始化框架基本功能
     */
    public static void init() {
        Class<?>[] classes = {
                ApplicationContext.class,
                DependencyInjection.class
        };
        for (Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName());
            } catch (ClassNotFoundException e) {
                throw new InitializationError("框架初始化错误");
            }
        }
    }
}
