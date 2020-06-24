package com.brick.core.bean.impl;

import com.brick.config.BrickConfig;
import com.brick.core.bean.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author maigeiye
 * @Description 实现了类扫描器接口的默认类扫描器
 * @version 1.0
 **/
public class DefaultClassScanner implements ClassScanner {

    private static final Logger logger = LoggerFactory.getLogger(DefaultClassScanner.class);

    /**
     * 维护一个包含所有项目类的list
     */
    private List<Class<?>> list;

    public List<Class<?>> getClassList() {
        // 除了第一次都不要需要扫描，直接返回
        if (list != null) {
            return list;
        }
        logger.info("开始扫描项目类");
        // 获取项目包名
        String packageName = BrickConfig.basePackage();
        List<Class<?>> classList = new ArrayList<Class<?>>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    // 在class目录中
                    String packagePath = url.getPath();
                    // 执行添加类操作
                    addClass(classList, packageName, packagePath);
                } else if (protocol.equals("jar")) {
                    // 在jar包中
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    Enumeration<JarEntry> jarEntries = jarFile.entries();
                    while (jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        // 判断entry是否为class
                        if (jarEntryName.endsWith(".class")) {
                            // 获取类名
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                            // 执行添加类操作
                            addClass(classList, className);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("类扫描失败", e);
        }
        // 扫描完成后保存list
        this.list = classList;
        logger.info("扫描项目类完成");
        return classList;
    }

    public Class<?> getClassBySuper(Class<?> superClass) {
        for (Class<?> clazz : list) {
            // 如果是superClass的子类就直接返回
            if (superClass.isAssignableFrom(clazz) && superClass != clazz) {
                return clazz;
            }
        }
        return null;
    }

    public void addClass(List<Class<?>> classList, final String packageName, String packagePath) {
        // 只获取包路径下的class文件和目录
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class") || pathname.isDirectory());
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                // 文件
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                className = packageName + "." + className;
                addClass(classList, className);
            } else {
                // 目录
                // 子目录名
                String subPackagePath = packagePath + "/" + fileName;
                // 子包名
                String subPackageName = packageName + "." + fileName;
                // 递归文件夹
                addClass(classList, subPackageName, subPackagePath);
            }
        }
    }

    public void addClass(List<Class<?>> classList, String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("加载类失败", e);
        }
        classList.add(clazz);
    }
}
