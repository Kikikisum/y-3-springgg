package com.example.y3spring.aop.support;

import com.example.y3spring.aop.ClassFilter;
import com.example.y3spring.aop.MethodMatcher;
import com.example.y3spring.aop.Pointcut;

import java.lang.reflect.Method;

public class DefaultPointcut implements Pointcut, ClassFilter, MethodMatcher {
    public static final DefaultPointcut INSTANCE = new DefaultPointcut();

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public boolean matches(Class<?> targetClass) {
        return true;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return true;
    }
}
