package com.example.y3spring.context.beans.factory.support;

import com.example.y3spring.context.beans.factory.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String,Object> beansRegistryMap = new ConcurrentHashMap<>();

    private final Map<Class<?>,String> beanNamesMap = new ConcurrentHashMap<>();

    @Override
    public void register(String name,Object bean){
        if(beansRegistryMap.containsKey(name))
        {
            throw new RuntimeException("注册表总存在同名bean，注册失败！！");
        }
        beansRegistryMap.putIfAbsent(name,bean);
        beanNamesMap.putIfAbsent(bean.getClass(),name);
    }

    @Override
    public Object getSingleBeanByClass(Class<?> beanClass)
    {
        String name=beanNamesMap.get(beanClass);
        if(name==null){
            throw new RuntimeException("没有该类型的bean!!");
        }
        Object bean=beansRegistryMap.get(name);
        if (bean==null){
            throw new RuntimeException("没有该类型的bean!!");
        }
        return bean;
    }

    @Override
    public Object getSingleBeanByName(String name){
        return beansRegistryMap.get(name);
    }

    @Override
    public void destroySingletonBeans() {
        beansRegistryMap.clear();
        beanNamesMap.clear();
    }
}
