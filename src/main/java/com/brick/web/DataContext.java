package com.brick.web;

import org.apache.commons.lang.ArrayUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author maigeiye
 * @Description Servlet数据调用类
 * @version 1.0
 **/
public class DataContext {

    private static final ThreadLocal<DataContext> dataContextContainer = new ThreadLocal<DataContext>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    public static void init(HttpServletRequest request, HttpServletResponse response) {
        DataContext dataContext = new DataContext();
        dataContext.request = request;
        dataContext.response = response;
        dataContextContainer.set(dataContext);
    }

    public static void destroy() {
        dataContextContainer.remove();
    }

    private static HttpServletRequest getRequest() {
        return dataContextContainer.get().request;
    }

    private static HttpServletResponse getResponse() {
        return dataContextContainer.get().response;
    }

    private static HttpSession getSession() {
        return getRequest().getSession();
    }

    private static ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

    /**
     * 封装request操作
     */
    public static class Request {
        public static void put(String key, Object value) {
            getRequest().setAttribute(key, value);
        }

        public static Object get(String key) {
            return getRequest().getAttribute(key);
        }

        public static void remove(String key) {
            getRequest().removeAttribute(key);
        }
    }

    /**
     * 封装response操作
     */
    public static class Response {
        public static void put(String key, Object value) {
            getResponse().setHeader(key, value.toString());
        }

        public static Object get(String key) {
            return getResponse().getHeader(key);
        }
    }

    /**
     * 封装session操作
     */
    public static class Session {
        public static void put(String key, Object value) {
            getSession().setAttribute(key, value);
        }

        public static Object get(String key) {
            return getSession().getAttribute(key);
        }

        public static void remove(String key) {
            getSession().removeAttribute(key);
        }

        public static void remove() {
            getSession().invalidate();
        }
    }

    /**
     * 封装ServletContext操作
     */
    public static class Context {
        public static void put(String key, Object value) {
            getServletContext().setAttribute(key, value);
        }

        public static void get(String key) {
            getServletContext().getAttribute(key);
        }

        public static void remove(String key) {
            getServletContext().removeAttribute(key);
        }
    }

    /**
     * 封装cookie操作
     */
    public static class Cookies {
        public static void put(String key, Object value) {
            Cookie cookie = new Cookie(key, value.toString());
            getResponse().addCookie(cookie);
        }

        public static Object get(String key) {
            Cookie[] cookies = getRequest().getCookies();
            if (ArrayUtils.isNotEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    if (key.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        }
    }
}
