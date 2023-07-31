package com.example.y3spring.beans.factory;

/**
 * 提供一次查询获取所有bean的接口
 */
public interface ListableBeanFactory extends BeanFactory{
    /**
     * 根据bean类型获取bean名字的序列
     * @param type bean类型
     * @param includeNonSingleton 是否有非单例的bean
     * @return bean名字的序列
     */
    String[] getBeanNamesForType(Class<?> type, boolean includeNonSingleton);
}
