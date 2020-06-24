package com.brick.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author maigeiye
 * @Description 属性文件工具类
 * @version 1.0
 **/
public class PropsUtil {
    /**
     * 加载属性文件
     */
    public static Properties loadProps(String propsPath) {
        Properties props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propsPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
