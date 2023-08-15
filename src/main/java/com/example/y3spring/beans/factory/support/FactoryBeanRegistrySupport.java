package com.example.y3spring.beans.factory.support;

import com.example.y3spring.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    private final Map<String,Object> factoryBeanObjectsCache = new ConcurrentHashMap<>(16);

    /**
     * 从缓存中为指定的FactoryBean获取内部的bean
     */
    protected Object getCachedObjectForFactoryBean(String beanName){
        return factoryBeanObjectsCache.get(beanName);
    }

    /**
     * 添加缓存
     */
    protected void addCachedObjectForFactoryBean(String beanName,Object bean){
        factoryBeanObjectsCache.putIfAbsent(beanName,bean);
    }

    /**
     * 从指定FactoryBean中获取内置bean
     */
    protected Object getObjectFromFactoryBean(String beanName, FactoryBean<?> factoryBean){

        if(factoryBean.isSingleton()){
            Object bean;
            synchronized (this.factoryBeanObjectsCache){
                bean = factoryBeanObjectsCache.get(beanName);
                if(bean != null){
                    return bean;
                }
                bean = factoryBean.getObject();
                factoryBeanObjectsCache.putIfAbsent(beanName,bean);

                return bean;
            }
        }else {
            return factoryBean.getObject();
        }
    }
}
