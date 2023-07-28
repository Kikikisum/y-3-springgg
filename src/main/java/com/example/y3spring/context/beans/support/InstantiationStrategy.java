package com.example.y3spring.context.beans.support;

import com.example.y3spring.context.beans.BeanDefinition;

public interface InstantiationStrategy {
    /**
     * 使用某种策略实例化Bean对象
     * @param beanDefinition
     * @param <T>
     * @return
     */
    <T> T instantiation(BeanDefinition<T> beanDefinition);
}
