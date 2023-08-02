package com.example.y3spring.aop;

public interface PointcutAdvisor extends Advisor{
    /**
     * 获取当前Advisor所匹配的切入点
     */
    PointCut getPointcut();
}
