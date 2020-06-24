package com.brick.config;

import com.brick.config.BrickSpecification;
import com.brick.utils.ConvertUtil;
import com.brick.utils.PropsUtil;

import java.util.Properties;

/**
 * @Author maigeiye
 * @Description 配置信息类
 * @version 1.0
 **/
public class BrickConfig {
    private static final Properties props = PropsUtil.loadProps(BrickSpecification.BRICK_CONFIG_FILE);

    public static String basePackage() {
        return props.getProperty("brick.base_package");
    }

    // 数据源有关配置
    public static String dsDriver() {
        return props.getProperty("brick.datasource.driver");
    }

    public static String dsUrl() {
        return props.getProperty("brick.datasource.url");
    }

    public static String dsUsername() {
        return props.getProperty("brick.datasource.username");
    }

    public static String dsPassword() {
        return props.getProperty("brick.datasource.password");
    }

    // 服务器有关配置
    public static int serverPort() {
        String port = props.getProperty("server.port");
        return port == null ? 8080 : (Integer) ConvertUtil.convert(int.class, port);
    }
}
