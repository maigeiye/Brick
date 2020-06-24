package com.brick;

import com.brick.config.BrickLoad;
import com.brick.web.server.Server;
import com.brick.web.server.TomcatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author maigeiye
 * @Description Brick启动器
 * @version 1.0
 **/
public class Brick {

    private static Server server;
    private static final Logger logger = LoggerFactory.getLogger(Brick.class);

    public static void run() {
        new Brick().start();
    }

    private void start() {
        try {
            BrickLoad.init();
            server = new TomcatServer();
            server.start();
        } catch (Exception e) {
            logger.error("Brick启动失败", e);
        }
    }
}
