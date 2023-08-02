package com.example.y3spring.aop.support;

import com.example.y3spring.aop.Pointcut;
import com.example.y3spring.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

public class DefaultPointcutAdvisor implements PointcutAdvisor {
    /**
     * 默认值为匹配所有类所有方法的切入点
     */
    private Pointcut pointcut = Pointcut.DEFAULT_POINT_CUT;

    private final Advice advice;

    public DefaultPointcutAdvisor(Advice advice){
        this.advice = advice;
    }

    public DefaultPointcutAdvisor(Pointcut pointcut,Advice advice){
        this.pointcut = pointcut;
        this.advice = advice;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
