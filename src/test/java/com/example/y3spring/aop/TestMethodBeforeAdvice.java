package com.example.y3spring.aop;

import java.lang.reflect.Method;

public class TestMethodBeforeAdvice implements MethodBeforeAdvice{

    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("测试方法执行前通知");
    }
}
