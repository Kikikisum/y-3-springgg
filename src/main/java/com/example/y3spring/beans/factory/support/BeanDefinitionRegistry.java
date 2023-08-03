package com.example.y3spring.beans.factory.support;

import com.example.y3spring.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    /**
     * 注册bean定义
     * @param name beanname
     * @param beanDefinition
     * @param <T>
     */
    <T> void registerBeanDefinition(String name, BeanDefinition<T> beanDefinition);

    /**
     * 在注册表中查询是否有同名的beandefinition
     * @param name bean名字
     * @return
     */
    boolean containsBeanDefinition(String name);

    /**
     * 根据bean的名字和类型获取该bean的定义
     * @param beanName bean的名字
     * @param beanClass bean的类型
     * @return
     */
    BeanDefinition<?> getBeanDefinition(String beanName,Class<?> beanClass);
}
