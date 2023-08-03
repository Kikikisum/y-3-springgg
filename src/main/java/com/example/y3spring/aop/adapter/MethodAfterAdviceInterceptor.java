package com.example.y3spring.aop.adapter;

import com.example.y3spring.aop.MethodAfterAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class MethodAfterAdviceInterceptor implements MethodInterceptor {
    private final MethodAfterAdvice advice;

    public MethodAfterAdviceInterceptor(MethodAfterAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object retVal;

        retVal = invocation.proceed();
        // 在调用完后再执行通知
        advice.after(invocation.getMethod(),invocation.getArguments(),invocation.getThis());

        return retVal;
    }
}
