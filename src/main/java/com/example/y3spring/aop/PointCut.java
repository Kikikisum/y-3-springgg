package com.example.y3spring.aop;

public interface PointCut {
    /**
     * 获取当前切入点的类过滤器
     */
    ClassFilter getClassFilter();

    /**
     * 获取当前切入点的方法过滤器
     */
    MethodMatcher getMethodMatcher();

}
