package com.example.y3spring.aop.framework;

import com.example.y3spring.aop.exception.AopConfigException;

import java.lang.reflect.Proxy;

public class DefaultAopProxyFactory implements AopProxyFactory{
    @Override
    public AopProxy createAopProxy(AdvisedSupport config) {

        if(config.isProxyTargetClass()){
            Class<?> targetClass = config.getTargetClass();
            if(targetClass == null){
                throw new AopConfigException("被代理的目标对象无法指定类型，或没有给定目标对象");
            }
            // 如果目标对象是个接口，或者已被JDK代理
            if(targetClass.isInterface() || Proxy.isProxyClass(targetClass)){
                return new JdkDynamicAopProxy(config);
            }

            return new CglibAopProxy(config);
        }
        return new JdkDynamicAopProxy(config);
    }
}
