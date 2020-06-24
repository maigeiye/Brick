package com.brick.core.exception;

/**
 * @Author maigeiye
 * @Description 初始化错误
 * @version 1.0
 **/
public class InitializationError extends Error {
    public InitializationError() {
        super();
    }

    public InitializationError(String message) {
        super(message);
    }

    public InitializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationError(Throwable cause) {
        super(cause);
    }
}
