package com.example.y3spring.beans.factory;

/**
 * 管理某个Bean的BeanFactory
 * @param <T>
 */
public interface FactoryBean<T> {
    /**
     * 获取内部bean
     */
    T getObject();

    /**
     * 获取内部bean的类型
     */
    Class<T> getObjectType();

    /**
     * 当前FactoryBean是否是单例的，默认为true
     */
    default boolean isSingleton(){
        return true;
    }
}
