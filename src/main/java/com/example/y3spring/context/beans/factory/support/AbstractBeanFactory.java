package com.example.y3spring.context.beans.factory.support;


import com.example.y3spring.context.beans.factory.BeanDefinition;
import com.example.y3spring.context.beans.factory.BeanFactory;
import com.example.y3spring.context.beans.factory.ConfigurableBeanFactory;
import com.example.y3spring.context.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.context.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.exception.BeansException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, ConfigurableBeanFactory {
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String beanName) {

        Object bean = getSingleBeanByName(beanName);
        // 如果bean为空，则说明注册表中没有 需要在工厂创建
        if(bean == null){
            // 双重校验
            synchronized (AbstractBeanFactory.class){
                bean = getSingleBeanByName(beanName);
                if(bean == null){
                    bean = createBean(getBeanDefinition(beanName));
                    // 注册进注册表
                    register(beanName,bean);
                }
            }
        }
        return bean;
    }

    /**
     * 创建名为beanName的Bean实例
     * 创建策略由实现类决定
     */
    protected abstract <T> T createBean(BeanDefinition<T> beanDefinition);

    /**
     * 根据beanName获取它的bean定义
     */
    protected abstract BeanDefinition<?> getBeanDefinition(String beanName);

    protected abstract <T> T createBean(String name, BeanDefinition<T> beanDefinition);

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public List<BeanFactoryPostProcessor> getBeanFactoryBeanPostProcessors() {
        return beanFactoryPostProcessors;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        if(beanFactoryPostProcessor==null)
        {
            throw new BeansException("beanPostProcessor不能为空!");
        }
        //删去之前的beanPostProcessor,添加新的
        beanPostProcessors.remove(beanFactoryPostProcessor);
        beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }
}
