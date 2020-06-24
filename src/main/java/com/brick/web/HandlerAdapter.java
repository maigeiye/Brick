package com.brick.web;

import com.brick.core.bean.ApplicationContext;
import com.brick.utils.ConvertUtil;
import com.brick.utils.JsonUtil;
import com.brick.web.annotation.ResponseBody;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author maigeiye
 * @Description Handler调用器
 * @version 1.0
 **/
public class HandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HandlerAdapter.class);

    /**
     * 执行Handler
     */
    public void invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws IllegalAccessException, InstantiationException {
        Map<String, String> requestParams = getRequestParams(request);
        List<Object> methodParams = getMethodParamsInstance(handler.getMethodParameter(), requestParams);
        Object controller = ApplicationContext.getBean(handler.getController());
        Method invokeMethod = handler.getMethod();
        // 取消类型安全检测
        invokeMethod.setAccessible(true);
        Object result;
        // 反射调用方法
        try {
            if (methodParams.size() == 0) {
                result = invokeMethod.invoke(controller);
            } else {
                result = invokeMethod.invoke(controller, methodParams.toArray());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
        // 解析方法的返回对象
        resultResolver(handler, result, request, response);
    }

    /**
     * 获取http请求参数
     */
    private Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        request.getParameterMap().forEach((paramName, paramValues) -> {
            if (ArrayUtils.isNotEmpty(paramValues)) {
                paramMap.put(paramName, paramValues[0]);
            }
        });
        return paramMap;
    }

    /**
     * 方法参数实例化
     */
    private List<Object> getMethodParamsInstance(Map<String, Class<?>> methodParams, Map<String, String> requestParams) {
        return methodParams.keySet().stream().map(paramName -> {
            Class<?> type = methodParams.get(paramName);
            String requestValue = requestParams.get(paramName);
            Object value;
            if (requestValue == null) {
                value = ConvertUtil.primitiveNull(type);
            } else {
                value = ConvertUtil.convert(type, requestValue);
            }
            return value;
        }).collect(Collectors.toList());
    }

    /**
     * Controller返回值解析
     */
    private void resultResolver(Handler handler, Object result, HttpServletRequest request, HttpServletResponse response) {
        if (result == null) {
            return;
        }
        boolean isJson = handler.getMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            // 如果返回json
            // 设置响应头
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter writer = response.getWriter()) {
                String s = JsonUtil.toJSONString(result);
                writer.write(s);
                writer.flush();
            } catch (IOException e) {
                logger.error("响应JSON数据失败");
            }
        } else {
            // 如果返回页面
            String path;
            if (result instanceof ModelAndView) {
                ModelAndView mv = (ModelAndView) result;
                path = mv.getView();
                Map<String, Object> model = mv.getModel();
                if (!model.isEmpty()) {
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        request.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
            } else if (result instanceof String) {
                path = (String) result;
            } else {
                throw new RuntimeException("返回类型不合法");
            }
            // 转发请求
            try {
                request.getRequestDispatcher("/templates/" + path).forward(request, response);
            } catch (Exception e) {
                logger.error("请求转发失败", e);
            }
        }
    }
}
