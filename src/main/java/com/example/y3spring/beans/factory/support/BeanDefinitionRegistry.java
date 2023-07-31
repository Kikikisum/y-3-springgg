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
}
