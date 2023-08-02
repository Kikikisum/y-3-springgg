package com.example.y3spring.aop.framework;

import com.example.y3spring.aop.TargetSource;
import com.example.y3spring.aop.exception.AopConfigException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


import java.lang.reflect.Method;
import java.util.List;

public class CglibAopProxy implements AopProxy{
    private final AdvisedSupport advisedSupport;

    public CglibAopProxy(AdvisedSupport config){
        this.advisedSupport = config;
    }
    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advisedSupport.getTargetClass());
        enhancer.setCallback(new CglibMethodInterceptor());

        return enhancer.create();
    }
    private class CglibMethodInterceptor implements MethodInterceptor {

        /**
         * 这里的实现和jdk动态代理一模一样
         */
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            TargetSource targetSource = advisedSupport.getTargetSource();
            Object target = targetSource.getTarget();
            Class<?> targetClass = targetSource.getTargetClass();

            List<Object> chain = advisedSupport.getAdvisorChainFactory().getInterceptorsAndDynamicInterceptionAdvice(advisedSupport, method, targetClass);

            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(obj, target, method, args, chain, proxy);

            Object retVal;

            if(chain.isEmpty()){
                retVal = methodInvocation.invokeJoinPoint();
            }else {
                retVal = methodInvocation.proceed();
            }
            return retVal;
        }
    }

    private class CglibMethodInvocation extends ReflectiveMethodInvocation{

        private final MethodProxy methodProxy;

        public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] args, List<Object> interceptorAndAdviceList,
                                     MethodProxy methodProxy) {
            super(proxy, target, method, args, interceptorAndAdviceList);
            this.methodProxy = methodProxy;
        }

        /**
         * 利用Cglib提供的原方法调用 效率比反射调用快
         */
        @Override
        public Object invokeJoinPoint() {
            try {
                return methodProxy.invoke(this.target,this.args);
            } catch (Throwable e) {
                throw new AopConfigException("被代理的原方法执行失败",e);
            }
        }
    }

}
