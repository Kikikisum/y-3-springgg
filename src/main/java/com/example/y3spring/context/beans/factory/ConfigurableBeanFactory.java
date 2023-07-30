package com.example.y3spring.context.beans.factory;

import com.example.y3spring.context.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.context.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.context.beans.factory.support.SingletonBeanRegistry;

import java.util.List;

/**
 * 对BeanFactory进行配置的接口
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {
    /**
     * 获取当前容器的BeanPostProcessor列表
     * @return
     */
    List<BeanPostProcessor> getBeanPostProcessors();

    /**
     * 获取当前容器的BeanFactoryPostProcessor列表
     * @return
     */
    List<BeanFactoryPostProcessor> getBeanFactoryBeanPostProcessors();

    /**
     * 向当前容器添加BeanPostProcessor
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 向当前容器添加BeanFactoryPostProcessor
     * @param beanFactoryPostProcessor
     */
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);

    /**
     * 毁灭BeanFactory的所有单例bean
     */
    void destroySingletons();

    /**
     * 预先实例化BeanFactory的所有bean
     */
    void preInitiateSingletons();

    /**
     * 对指定的bean执行毁灭
     * @param beanName
     * @param bean
     */
    void destroySingleton(String beanName,Object bean);
}
