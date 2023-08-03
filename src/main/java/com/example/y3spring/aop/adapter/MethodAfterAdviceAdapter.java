package com.example.y3spring.aop.adapter;

import com.example.y3spring.aop.Advisor;
import com.example.y3spring.aop.MethodAfterAdvice;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * 用于适配MethodAfterAdvice的适配器
 */
public class MethodAfterAdviceAdapter implements AdvisorAdapter{
    @Override
    public MethodInterceptor getInterceptor(Advisor advisor) {
        return new MethodAfterAdviceInterceptor((MethodAfterAdvice) advisor.getAdvice());
    }

    @Override
    public boolean supportAdvice(Advice advice) {
        return (advice instanceof MethodAfterAdvice);
    }
}
