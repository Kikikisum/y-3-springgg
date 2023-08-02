package com.example.y3spring.aop.framework;

import com.example.y3spring.aop.TargetSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 使用JDK的动态代理实现的AopProxy代理类，用于生成包含指定逻辑的代理对象
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    /**
     * 用于配置代理类
     */
    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),advisedSupport.getInterfaces(), this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        TargetSource targetSource = advisedSupport.getTargetSource();
        Object target = targetSource.getTarget();



        return null;
    }
}
