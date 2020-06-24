package com.brick.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author maigeiye
 * @Description Json转换工具类
 * @version 1.0
 **/
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper objectmapper = new ObjectMapper();

    /**
     * Java对象转为JSON字符串
     */
    public static <T> String toJSONString(T object) {
        String jsonString;
        try {
            jsonString = objectmapper.writeValueAsString(object);
            return jsonString;
        } catch (Exception e) {
            logger.error("Java对象转换JSON出错", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON字符串转为Java对象
     */
    public static <T> T parseObject(String jsonString, Class<T> type) {
        T obj;
        try {
            obj = objectmapper.readValue(jsonString, type);
            return obj;
        } catch (Exception e) {
            logger.error("JSON转换java对象出错", e);
            throw new RuntimeException(e);
        }
    }
}
