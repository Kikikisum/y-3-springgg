package com.example.y3spring.beans.factory.config;

import com.example.y3spring.beans.factory.ConfigurableBeanFactory;

/**
 * BeanFactory的后置处理器
 */
public interface BeanFactoryPostProcessor {
    /**
     * 在创造bean实例前调用，修改指定的beanDefinition
     * @param beanFactory 工厂对象
     */
    void postProcessBeanFactory(ConfigurableBeanFactory beanFactory);
}
