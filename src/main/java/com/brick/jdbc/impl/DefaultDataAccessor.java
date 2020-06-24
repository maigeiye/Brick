package com.brick.jdbc.impl;

import com.brick.jdbc.DataAccessor;
import com.brick.jdbc.DataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author maigeiye
 * @Description 数据访问器默认实现
 * @version 1.0
 **/
public class DefaultDataAccessor implements DataAccessor {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDataAccessor.class);

    private QueryRunner queryRunner;

    public DefaultDataAccessor() {
        DataSource dataSource = DataSourceFactory.createDataSource();
        queryRunner = new QueryRunner(dataSource);
    }

    public <T> T query(Class<T> entity, String sql, Object... params) {
        T result;
        try {
            result = queryRunner.query(sql, new BeanHandler<T>(entity), params);
        } catch (SQLException e) {
            logger.error("查询失败", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public <T> List<T> queryList(Class<T> entity, String sql, Object... params) {
        List<T> result;
        try {
            result = (List<T>) queryRunner.query(sql, new BeanHandler<T>(entity), params);
        } catch (SQLException e) {
            logger.error("查询失败", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public int update(String sql, Object... params) {
        int result;
        try {
            result = queryRunner.update(sql, params);
        } catch (SQLException e) {
            logger.error("更新失败", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public void insert(String sql, Object... params) {
            try {
                queryRunner.update(sql, params);
            } catch (SQLException e) {
                logger.error("插入失败", e);
                throw new RuntimeException(e);
            }
    }

    public void delete(String sql, Object... params) {
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            logger.error("删除失败", e);
            throw new RuntimeException(e);
        }
    }
}
