package com.example.y3spring.aop.aspectj;

import com.example.y3spring.aop.Pointcut;
import com.example.y3spring.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    private Advice advice;
    private Pointcut pointcut;

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

    public void setPointcut(Pointcut pointcut){
        this.pointcut=pointcut;
    }

    public void setAdvice(Advice advice){
        this.advice=advice;
    }

}
