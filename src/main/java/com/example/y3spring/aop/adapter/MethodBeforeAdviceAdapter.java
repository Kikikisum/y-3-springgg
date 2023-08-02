package com.example.y3spring.aop.adapter;


import com.example.y3spring.aop.Advisor;
import com.example.y3spring.aop.MethodBeforeAdvice;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * 用于向Spring适配 {@link MethodBeforeAdvice} 和 {@link MethodBeforeAdviceInterceptor} 的适配器
 */
public class MethodBeforeAdviceAdapter implements AdvisorAdapter{
    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        MethodBeforeAdvice advice = (MethodBeforeAdvice) advisor.getAdvice();
        return new MethodBeforeAdviceInterceptor(advice);
    }

    @Override
    public boolean supportAdvice(Advice advice) {
        return (advice instanceof MethodBeforeAdvice);
    }
}
