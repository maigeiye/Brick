package com.brick.core.bean;

import java.util.List;

/**
 * @Author maigeiye
 * @Description 类扫描器
 * @version 1.0
 **/
public interface ClassScanner {
    /**
     * 获取类路径下的所有类
     */
    List<Class<?>> getClassList();

    /**
     * 根据父类得到累路径下的子类，获取第一个
     */
    Class<?> getClassBySuper(Class<?> superClass);
}
