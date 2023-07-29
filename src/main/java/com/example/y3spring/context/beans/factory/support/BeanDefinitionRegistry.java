package com.example.y3spring.context.beans.factory.support;

import com.example.y3spring.context.beans.factory.BeanDefinition;

public interface BeanDefinitionRegistry {
    /**
     * 注册bean定义
     * @param name beanname
     * @param beanDefinition
     * @param <T>
     */
    <T> void registerBeanDefinition(String name, BeanDefinition<T> beanDefinition);
}
