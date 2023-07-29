package com.example.y3spring.context.beans.factory.config;

import com.example.y3spring.context.beans.factory.BeanDefinition;

public interface InstantiationStrategy {
    /**
     * 使用某种策略实例化Bean对象
     * @param beanDefinition
     * @param <T>
     * @return
     */
    <T> T instantiation(BeanDefinition<T> beanDefinition);
}
