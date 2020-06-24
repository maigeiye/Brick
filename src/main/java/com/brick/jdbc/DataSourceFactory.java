package com.brick.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.brick.config.BrickConfig;
import com.brick.core.exception.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author maigeiye
 * @Description 数据库连接池工厂类
 * @version 1.0
 **/
public class DataSourceFactory {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);
    private static final String driver = BrickConfig.dsDriver();
    private static final String url = BrickConfig.dsUrl();
    private static final String userName = BrickConfig.dsUsername();
    private static final String password = BrickConfig.dsPassword();

    // 根据配置信息创建一个数据库连接池
    public static DruidDataSource createDataSource() {
        DruidDataSource dataSource = null;
        try {
            dataSource = new DruidDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
        } catch (Exception e) {
            throw new InitializationError("数据库连接池初始化失败");
        }
        logger.info("数据库连接池初始化成功");
        return dataSource;
    }
}
