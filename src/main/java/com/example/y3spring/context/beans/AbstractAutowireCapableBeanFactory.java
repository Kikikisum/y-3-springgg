package com.example.y3spring.context.beans;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    @Override
    protected <T> T createBean(String beanName, BeanDefinition<T> beanDefinition) {
        return doCreate(beanName,beanDefinition);
    }

    /**
     * 创建Bean实例的实际逻辑，由子类实现
     * @param beanName
     * @param beanDefinition
     * @param <T>
     * @return
     */
    public abstract <T> T doCreate(String beanName,BeanDefinition<T> beanDefinition);
}
