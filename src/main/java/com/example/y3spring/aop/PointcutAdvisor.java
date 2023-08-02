package com.example.y3spring.aop;

import org.aopalliance.aop.Advice;

public interface PointcutAdvisor extends Advisor, org.springframework.aop.Advisor {
    /**
     * 获取当前Advisor所匹配的切入点
     */
    Pointcut getPointcut();

    @Override
    Advice getAdvice();
}
