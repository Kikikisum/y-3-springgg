package com.example.y3spring.aop.framework;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 用于通知链的工厂接口
 */
public interface AdvisorChainFactory {
    /**
     * 根据提供的aop配置类获取其拦截器列表
     * @param config aop配置类
     * @param method 要被aop增强的方法
     * @param targetClass 目标类
     * @return 拦截器列表
     */
    List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass);
}
