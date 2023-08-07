package com.example.y3spring.beans.factory.config;

import com.example.y3spring.beans.factory.BeanFactory;

/**
 * 提供一次查询获取所有bean的接口
 */
public interface ListableBeanFactory extends BeanFactory {
    /**
     * 根据bean类型获取bean名字的序列
     * @param type bean类型
     * @param includeNonSingleton 是否有非单例的bean
     * @return bean名字的序列
     */
    String[] getBeanNamesForType(Class<?> type, boolean includeNonSingleton);

    /**
     * 获取BeanFactory中所有BeanDefinition的名字
     */
    String[] getBeanDefinitionNames();

    /**
     * 根据bean的名字获取它的bean定义
     * @param beanName 要获取的bean的名字
     * @return bean的定义
     */
    BeanDefinition<?> getBeanDefinitionByName(String beanName);
}
