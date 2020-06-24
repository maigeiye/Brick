package com.brick.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author maigeiye
 * @Description 前端控制器
 * @version 1.0
 **/
public class DispatcherServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private HandlerMapping handlerMapping = new HandlerMapping();
    private HandlerAdapter handlerAdapter = new HandlerAdapter();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String requestMethod = req.getMethod();
        String requestPath = req.getPathInfo();
        logger.debug("{}:{}", requestMethod, requestPath);
        if (requestPath.endsWith("/")) {
            requestPath = requestPath.substring(0, requestPath.length() - 1);
        }
        // 获取Handler
        Handler handler = handlerMapping.getHandler(requestPath);
        // 未找到Controller，返回404
        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 初始化DataContext
        DataContext.init(req, resp);
        try {
            handlerAdapter.invokeHandler(req, resp, handler);
        } catch (Exception e) {
            throw new RuntimeException("调用handler失败", e);
        } finally {
            // 销毁DataContext
            DataContext.destroy();
        }
    }
}
