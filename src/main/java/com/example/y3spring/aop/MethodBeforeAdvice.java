package com.example.y3spring.aop;

import java.lang.reflect.Method;

public interface MethodBeforeAdvice extends BeforeAdvice{
    /**
     * 在调用通知所在的方法（连接点）前 会调用此方法，用于增强方法逻辑
     * @param method 要调用的方法
     * @param args 参数表
     */
    void before(Method method, Object[] args,Object target);
}
