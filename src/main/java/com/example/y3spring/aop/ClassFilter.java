package com.example.y3spring.aop;

/**
 * 类过滤器，是PointCut的一部分
 */
public interface ClassFilter {
    /**
     * 检查目标类是否匹配
     * @param targetClass 目标类
     * @return true-匹配  false-不匹配
     */
    boolean matches(Class<?> targetClass);
}
