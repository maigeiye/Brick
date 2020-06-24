package com.brick.web;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author maigeiye
 * @Description 处理请求结果数据封装类
 * @version 1.0
 **/
public class ModelAndView {
    /**
     * 页面路径
     */
    private String view;

    /**
     * 页面数据
     */
    private Map<String, Object> model = new HashMap<String, Object>();

    private ModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    public String getView() {
        return view;
    }

    public ModelAndView addObject(String name, Object value) {
        model.put(name, value);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
