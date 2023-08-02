package com.example.y3spring.aop.framework;

import com.example.y3spring.aop.exception.AopConfigException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation {
    protected final Object proxy;
    protected final Object target;
    protected final Method method;
    protected final Object[] args;
    protected final List<Object> interceptorAndAdviceList;

    protected int interceptorsIndex = 0;

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
            return invokeJoinPoint();
        }

        //todo 这里的返回值怎么办？只会有一个有效的返回值
        MethodInterceptor methodInterceptor = (MethodInterceptor) interceptorAndAdviceList.get(interceptorsIndex++);

        // 拦截器invoke里面会递归调用下一个拦截器
        return methodInterceptor.invoke(this);
    }
    /**
     * 执行连接点的原方法
     */
    public Object invokeJoinPoint(){
        this.method.setAccessible(true);
        try {
            return this.method.invoke(target,args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AopConfigException("连接点的方法执行错误",e);
        }
    }
}
