package com.example.y3spring.context.beans;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private static final Map<String,Object> beanMap=new HashMap<>();
    private static final Map<Class<?>,String> beanNameMap=new HashMap<>();

    @Override
    public void register(String name,Object bean){
        if(beanMap.containsKey(name))
        {
            throw new RuntimeException("注册表总存在同名bean，注册失败！！");
        }
        beanMap.putIfAbsent(name,bean);
        beanNameMap.putIfAbsent(bean.getClass(),name);
    }

    @Override
    public Object getSingleBeanByClass(Class<?> beanClass)
    {
        String name=beanNameMap.get(beanClass);
        if(name==null){
            throw new RuntimeException("没有该类型的bean!!");
        }
        Object bean=beanMap.get(name);
        if (bean==null){
            throw new RuntimeException("没有该类型的bean!!");
        }
        return bean;
    }

    @Override
    public Object getSingleBeanByName(String name){
        return beanMap.get(name);
    }

}
