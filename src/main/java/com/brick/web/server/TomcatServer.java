package com.brick.web.server;

import com.brick.config.BrickConfig;
import com.brick.core.exception.InitializationError;
import com.brick.web.DispatcherServlet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.apache.jasper.servlet.JspServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @Author maigeiye
 * @Description Tomcat服务器配置
 * @version 1.0
 **/
public class TomcatServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(TomcatServer.class);
    private Tomcat tomcat;

    public TomcatServer() {
        try {
            this.tomcat = new Tomcat();
            tomcat.setBaseDir("");
            tomcat.setPort(BrickConfig.serverPort());
            tomcat.setSilent(true);

            File root = getRootFolder();
            StandardContext context = (StandardContext) tomcat.addWebapp("", root.getAbsolutePath());
            context.setParentClassLoader(this.getClass().getClassLoader());

            tomcat.addServlet("", "jspServlet", new JspServlet()).setLoadOnStartup(3);
            tomcat.addServlet("", "defaultServlet", new DefaultServlet()).setLoadOnStartup(1);
            tomcat.addServlet("", "dispatcherServlet", new DispatcherServlet()).setLoadOnStartup(0);
            context.addServletMapping("/templates/" + "*", "jspServlet");
            context.addServletMapping("/static/" + "*", "defaultServlet");
            context.addServletMapping("/*", "dispatcherServlet");
        } catch (Exception e) {
            logger.error("tomcat初始化失败", e);
            throw new InitializationError(e);
        }
    }

    public void start() throws Exception {
        tomcat.start();
        String address = tomcat.getServer().getAddress();
        int port = tomcat.getConnector().getPort();
        logger.info("Tomcat启动在：http://{}:{}", address, port);
        tomcat.getServer().await();
    }

    public void stop() throws Exception {
        tomcat.stop();
    }

    private File getRootFolder() {
        try {
            File root;
            String runningJarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            return root;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
