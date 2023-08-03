package com.example.y3spring.aop;

import java.lang.reflect.Method;

/**
 * 表示在调用方法之后要执行的通知
 */
public interface MethodAfterAdvice extends AfterAdvice{
    /**
     * 在调用通知所在的连接点(方法）之后 会调用此方法，用于增强方法逻辑
     * @param method 要调用的方法
     * @param args 参数表
     * @param target 要调用方法的目标对象
     */
    void after(Method method, Object[] args, Object target);
}
