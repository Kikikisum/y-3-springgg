package com.example.y3spring.context.beans.factory;


import com.example.y3spring.context.beans.factory.support.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry,ConfigurableListableBeanFactory {
    /**
     * bean定义的注册表
     */
    private static final Map<String, BeanDefinition<?>> BEAN_DEFINITION_REGISTRY = new HashMap<>();

    @Override
    protected BeanDefinition<?> getBeanDefinition(String beanName) {
        if(beanName==null){
            throw new IllegalArgumentException("beanName不能为空");
        }

        BeanDefinition<?> beanDefinition = BEAN_DEFINITION_REGISTRY.get(beanName);

        if(beanDefinition == null){
            throw new IllegalArgumentException("不存在该beanName的bean定义");
        }

        return beanDefinition;
    }

    @Override
    public <T> void registerBeanDefinition(String beanName, BeanDefinition<T> beanDefinition) {
        if(beanName==null){
            throw new IllegalArgumentException("beanName不能为空");
        }
        BEAN_DEFINITION_REGISTRY.put(beanName,beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return BEAN_DEFINITION_REGISTRY.containsKey(beanName);
    }

}
