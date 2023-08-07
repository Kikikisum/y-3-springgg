package com.example.y3spring.beans.factory;


import com.example.y3spring.beans.factory.config.ListableBeanFactory;

/**
 * 整合BeanFactory所需的所有特性的接口
 */
public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory,AutowireCapableBeanFactory, ListableBeanFactory {
}
