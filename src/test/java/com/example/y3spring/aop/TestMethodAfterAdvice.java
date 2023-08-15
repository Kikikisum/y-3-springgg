package com.example.y3spring.aop;

import java.lang.reflect.Method;

public class TestMethodAfterAdvice implements MethodAfterAdvice{

    @Override
    public void after(Method method, Object[] args, Object target) {
        System.out.println("方法执行后调用通知逻辑");
    }
}
