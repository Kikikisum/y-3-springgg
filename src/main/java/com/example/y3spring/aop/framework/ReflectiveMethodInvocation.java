package com.example.y3spring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation {
    private final Object proxy;
    private final Object target;
    private final Method method;
    private final Object[] args;
    private final List<Object> interceptorAndAdviceList;

    private int interceptorsIndex = 0;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] args,
                                      List<Object> interceptorAndAdviceList){
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.args = args;
        this.interceptorAndAdviceList = interceptorAndAdviceList;
    }
    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.args;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return null;
    }

    @Override
    public Object proceed() throws Throwable {
        // 拦截器链执行完毕
        if(interceptorsIndex >= interceptorAndAdviceList.size()){
            // 执行原方法
            method.setAccessible(true);
            return method.invoke(target,args);
        }

        //todo 这里的返回值怎么办？只会有一个有效的返回值
        MethodInterceptor methodInterceptor = (MethodInterceptor) interceptorAndAdviceList.get(interceptorsIndex++);

        // 拦截器invoke里面会递归调用下一个拦截器
        return methodInterceptor.invoke(this);
    }
}
