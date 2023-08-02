package com.example.y3spring.aop.framework;

import com.example.y3spring.aop.TargetSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

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
        Class<?> targetClass = targetSource.getTargetClass();
        Class<?> actualClass = (targetClass != null ? targetClass : target.getClass());
        Object retVal;

        // 获取连接点目标对象的拦截器链
        List<Object> chain = advisedSupport.getAdvisorChainFactory().getInterceptorsAndDynamicInterceptionAdvice(advisedSupport, method, actualClass);

        // 若拦截器链为空 则直接执行原方法
        if(chain.isEmpty()){

            method.setAccessible(true);
            retVal = method.invoke(target,args);

        }else{
            // 否则执行拦截器链 再执行原方法
            ReflectiveMethodInvocation reflectiveMethodInvocation = new ReflectiveMethodInvocation(proxy, target, method, args, chain);
            retVal = reflectiveMethodInvocation.proceed();
        }
        return retVal;
    }
}
