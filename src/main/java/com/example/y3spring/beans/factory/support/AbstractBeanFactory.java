package com.example.y3spring.beans.factory.support;


import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.beans.factory.ConfigurableBeanFactory;
import com.example.y3spring.beans.factory.FactoryBean;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.beans.factory.exception.BeansException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    protected Object adaptBeanInstance(String beanName, Object beanInstance, Class<?> requiredType) {
        // 如果bean不是指定类型的实例 需要判断能否转化成指定类型
        if(requiredType != null && !requiredType.isInstance(beanInstance)){
            // 判断能够转化为对应类型的对象

            // 如果bean实例是目标类型的子类
            if(requiredType.isAssignableFrom(beanInstance.getClass())){
                return requiredType.cast(beanInstance);
            }
        }
        return beanInstance;
    }

    protected Object doGetBean(String beanName, Class<?> requiredType){
        Object beanInstance = null;
        Object sharedInstance = null;

        BeanDefinition<?> beanDefinition = getBeanDefinition(beanName);
        if(beanDefinition == null){
            log.debug("The bean Definition for the bean named [" + beanName + "] could not be found");
            return null;
        }

        if(beanDefinition.isSingleton()){
            // 在缓存中获取共享的bean实例
            sharedInstance = getSingleton(beanName);
        }

        if(sharedInstance != null){
            beanInstance = getObjectForBeanInstance(sharedInstance,beanName);
        }
        // 否则要准备创建新的bean实例
        else {
            beanInstance = createBean(beanName,beanDefinition);

            // 单例 需要缓存
            if(beanDefinition.isSingleton()){
                addSingleton(beanName,beanInstance);
            }

            beanInstance = getObjectForBeanInstance(beanInstance,beanName);
        }

        return adaptBeanInstance(beanName,beanInstance,requiredType);
    }
    /**
     * 从bean实例中获取bean
     * 用于特殊bean的处理（如FactoryBean、prototype作用域的bean等）
     * @param beanInstance bean实例
     */
    protected Object getObjectForBeanInstance(Object beanInstance,String beanName){

        Object instance = null;
        // 如果该实例是FactoryBean，则获取内置的bean
        if(beanInstance instanceof FactoryBean){
            // 先从缓存中获取
            //Object cacheInstance = getCachedObjectForFactoryBean(beanName);
            //if(cacheInstance != null){
            //    return cacheInstance;
            //}
            // 若缓存中没有
            FactoryBean<?> factory = (FactoryBean<?>) beanInstance;

            //instance = getObjectFromFactoryBean(beanName,factory);

        }else {
            instance = beanInstance;
        }

        return instance;
    }
}
