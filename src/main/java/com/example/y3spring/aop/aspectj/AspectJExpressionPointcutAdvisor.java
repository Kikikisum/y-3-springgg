package com.example.y3spring.aop.aspectj;

import com.example.y3spring.aop.Pointcut;
import com.example.y3spring.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    private final Advice advice;
    private final Pointcut pointcut;

    public AspectJExpressionPointcutAdvisor(Advice advice) {
        this.advice = advice;
        this.pointcut = Pointcut.DEFAULT_POINT_CUT;
    }
    public AspectJExpressionPointcutAdvisor(Advice advice,AspectJExpressionPointcut pointcut){
        this.advice = advice;
        this.pointcut = pointcut;
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
