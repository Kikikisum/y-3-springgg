package com.example.y3spring.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器，PointCut的一部分
 */
public interface MethodMatcher {
    /**
     * 检查目标类的目标方法是否匹配
     * @param method 目标方法
     * @param targetClass 目标类
     * @return 匹配的结果
     */
    boolean matches(Method method, Class<?> targetClass);
}
