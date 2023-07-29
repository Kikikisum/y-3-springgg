package com.example.y3spring.context.beans.factory;

/**
 * bean实例的单例注册表
 */
public interface SingletonBeanRegistry {
    /**
     * 注册bean到注册表中
     * @param name bean的名字
     * @param bean bean的实例对象
     */
    void register(String name,Object bean);

    /**
     * 根据bean类型来获取实例对象
     * @param beanClass bean类型
     * @return bean的实例
     */
    Object getSingleBeanByClass(Class<?> beanClass);

    /**
     * 根据bean的名字返回bean的实例对象
     * @param name bean的名字
     * @return bean的实例
     */
    Object getSingleBeanByName(String name);
}
