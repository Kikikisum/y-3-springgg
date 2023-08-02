package com.example.y3spring.aop.framework;

/**
 * Aop代理对象的工厂接口
 */
public interface AopProxyFactory {
    /**
     * 根据Aop代理配置创建一个代理
     * @param config Aop代理的配置类
     * @return 代理对象
     */
    AopProxy createAopProxy(AdvisedSupport config);
}
