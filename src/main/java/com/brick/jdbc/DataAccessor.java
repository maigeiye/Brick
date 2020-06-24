package com.brick.jdbc;

import java.io.Serializable;
import java.util.List;

/**
 * @Author maigeiye
 * @Description
 * @version 1.0
 **/
public interface DataAccessor {

    /**
     * 查询单条记录
     */
    <T> T query(Class<T> entity, String sql, Object... params);

    /**
     * 查询多条记录，返回列表
     */
    <T> List<T> queryList(Class<T> entity, String sql, Object... params);

    /**
     * 更新操作，返回更新记录数
     */
    int update(String sql, Object... params);

    /**
     * 插入一条记录
     */
    void insert(String sql, Object... params);

    /**
     * 删除一条数据
     */
    void delete(String sql, Object... params);
}