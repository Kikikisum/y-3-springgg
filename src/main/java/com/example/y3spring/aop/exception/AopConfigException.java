package com.example.y3spring.aop.exception;

/**
 * 在Aop配置过程中出现的异常
 */
public class AopConfigException extends RuntimeException{
    public AopConfigException() {
    }

    public AopConfigException(String message) {
        super(message);
    }

    public AopConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
