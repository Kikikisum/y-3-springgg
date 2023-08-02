package com.example.y3spring.aop;

import com.example.y3spring.aop.support.DefaultPointcut;

public interface Pointcut {
    /**
     * 获取当前切入点的类过滤器
     */
    ClassFilter getClassFilter();

    /**
     * 获取当前切入点的方法过滤器
     */
    MethodMatcher getMethodMatcher();

    /**
     * 默认的切入点
     */
    Pointcut DEFAULT_POINT_CUT = DefaultPointcut.INSTANCE;
}
