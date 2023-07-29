package com.example.y3spring.context.beans.factory;

/**
 * 为BeanFactory提供自动装配Bean属性的接口
 */
public interface AutowireCapableBeanFactory extends BeanFactory{

    /**
     * 为Bean实例对象自动装配属性
     * @param existingBean bean实例
     * @param beanDefinition bean定义
     */
    <T> void autoWirePropertyValues(T existingBean, BeanDefinition<T> beanDefinition);
}
