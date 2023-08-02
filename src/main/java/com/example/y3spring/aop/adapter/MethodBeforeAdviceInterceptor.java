package com.example.y3spring.aop.adapter;

import com.example.y3spring.aop.BeforeAdvice;
import com.example.y3spring.aop.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 对象适配器，包装了一个MethodBeforeAdvice
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice {
    private final MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice){
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 执行连接点的逻辑之前 执行当前通知的逻辑
        this.advice.before(invocation.getMethod(),invocation.getArguments());
        // 交给下一个拦截器
        return invocation.proceed();
    }
}
