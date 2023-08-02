package com.example.y3spring.aop.support;

import com.example.y3spring.aop.PointCut;

/**
 * PointCut的子接口，以表达式为主体
 */
public interface ExpressionPointcut extends PointCut {
    /**
     * 获取当前切入点的表达式
     * @return
     */
    String getExpression();
}
