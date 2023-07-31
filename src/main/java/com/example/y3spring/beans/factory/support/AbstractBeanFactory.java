package com.example.y3spring.beans.factory.support;


import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.beans.factory.ConfigurableBeanFactory;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.beans.factory.exception.BeansException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, ConfigurableBeanFactory {
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

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
    @Override
    public void destroySingleton(String beanName, Object bean) {
        // 委托给DisposableBeanAdapter进行处理
        new DisposableBeanAdapter(beanName,bean,getBeanDefinition(beanName)).destroy();
    }

    @Override
    public Object getBean(String beanName) {

        Object bean = null;

        BeanDefinition<?> beanDefinition = getBeanDefinition(beanName);

        // 单例模式
        if(beanDefinition.isSingleton()){
            // 先查看缓存中有无该bean
            bean = getSingleton(beanName);

            // 如果bean为空，则说明缓存注册表中没有 需要在工厂中创建一个新的实例
            if(bean == null){
                synchronized (AbstractBeanFactory.class){
                    bean = createBean(beanName, beanDefinition);
                    addSingleton(beanName,bean);
                }
            }
            // 多例bean 创建一个新实例
        }else if(beanDefinition.isPrototype()){
            bean = createBean(beanName,beanDefinition);
        }

        if(bean == null){
            throw new BeansException("The scope of the bean [" + beanName + "] is invalid");
        }

        return bean;
    }
}
