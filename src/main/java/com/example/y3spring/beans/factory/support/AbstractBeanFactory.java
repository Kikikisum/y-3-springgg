package com.example.y3spring.beans.factory.support;


import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.beans.factory.ConfigurableBeanFactory;
import com.example.y3spring.beans.factory.FactoryBean;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.example.y3spring.beans.factory.exception.BeansException;
import com.example.y3spring.beans.factory.utils.StringValueResolver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements BeanFactory, ConfigurableBeanFactory {
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    private final List<StringValueResolver> embeddedStringResolvers = new ArrayList<>();

    private List<InstantiationAwareBeanPostProcessor> instantiationAwareBeanPostProcessorCache;

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName,null);
    }

    @Override
    public Object getBean(String beanName, Class<?> requiredType) {
        return doGetBean(beanName,requiredType);
    }

    protected Object doGetBean(String beanName, Class<?> requiredType){

        Object beanInstance;
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

    /**
     * 创建名为beanName的Bean实例
     * 创建策略由实现类决定
     */
    protected abstract Object createBean(String beanName,BeanDefinition<?> beanDefinition);

    /**
     * 根据beanName获取它的bean定义
     */
    protected abstract BeanDefinition<?> getBeanDefinition(String beanName);

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        if(beanPostProcessor == null){
            throw new BeansException("添加的beanPostProcessor不能为空");
        }
        // 把旧的删掉
        beanPostProcessors.remove(beanPostProcessor);
        // 添加新的
        beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public List<BeanFactoryPostProcessor> getBeanFactoryBeanPostProcessors() {
        return beanFactoryPostProcessors;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
        beanFactoryPostProcessors.add(beanFactoryPostProcessor);
    }


    /**
     * 从bean实例中获取bean
     * 用于特殊bean的处理（如FactoryBean、prototype作用域的bean等）
     * @param beanInstance bean实例
     */
    protected Object getObjectForBeanInstance(Object beanInstance,String beanName){

        Object instance;
        // 如果该实例是FactoryBean，则获取内置的bean
        if(beanInstance instanceof FactoryBean){
            // 先从缓存中获取
            Object cacheInstance = getCachedObjectForFactoryBean(beanName);
            if(cacheInstance != null){
                return cacheInstance;
            }
            // 若缓存中没有
            FactoryBean<?> factory = (FactoryBean<?>) beanInstance;

            instance = getObjectFromFactoryBean(beanName,factory);

        }else {
            instance = beanInstance;
        }

        return instance;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        embeddedStringResolvers.add(valueResolver);
    }

    @Override
    public StringValueResolver[] getEmbeddedValueResolvers() {
        return embeddedStringResolvers.toArray(new StringValueResolver[0]);
    }

    /**
     * 检查当前BeanFactory中是否有注册InstantiationAwareBeanPostProcess
     * @return
     */
    protected boolean hasInstantiationAwareBeanPostProcessors(){
        // cache为空 重新获取
        if(instantiationAwareBeanPostProcessorCache == null){
            getBeanPostProcessorsCache();
        }
        return !this.instantiationAwareBeanPostProcessorCache.isEmpty();
    }

    protected void getBeanPostProcessorsCache(){

        this.instantiationAwareBeanPostProcessorCache = new ArrayList<>();
        for (BeanPostProcessor beanPostProcessor : this.beanPostProcessors) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                this.instantiationAwareBeanPostProcessorCache.add((InstantiationAwareBeanPostProcessor) beanPostProcessor);
            }
        }

    }

    public List<InstantiationAwareBeanPostProcessor> getInstantiationAwareBeanPostProcessorCache() {
        return instantiationAwareBeanPostProcessorCache;
    }
}
