package com.example.y3spring.beans.factory;

import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.beans.factory.config.PropertyValues;
import org.springframework.lang.Nullable;

/**
 * 为BeanFactory提供自动装配Bean属性的接口
 */
public interface AutowireCapableBeanFactory extends BeanFactory{

    /**
     * 为Bean实例对象自动装配属性
     * @param name bean名字
     * @param existingBean bean实例
     * @param beanDefinition bean定义
     */
    void autoWirePropertyValues(String name, Object existingBean, BeanDefinition<?> beanDefinition, @Nullable PropertyValues pvs);
}
