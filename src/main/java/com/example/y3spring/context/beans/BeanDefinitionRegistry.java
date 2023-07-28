package com.example.y3spring.context.beans;

public interface BeanDefinitionRegistry {
    /**
     * 注册bean定义
     * @param name beanname
     * @param beanDefinition
     * @param <T>
     */
    <T> void registerBeanDefinition(String name,BeanDefinition<T> beanDefinition);
}
