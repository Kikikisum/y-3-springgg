package com.example.y3spring.beans.factory;

import com.example.y3spring.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.beans.factory.support.SingletonBeanRegistry;
import com.example.y3spring.beans.factory.utils.StringValueResolver;

import java.util.List;

/**
 * 对BeanFactory进行配置的接口
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

    /**
     * 单例作用域的标识符
     */
    String SCOPE_SINGLETON = "singleton";
    /**
     * 多例作用域的标识符
     */
    String SCOPE_PROTOTYPE = "prototype";

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

    /**
     * 往当前BeanFactory中添加指定的嵌入式字符串处理器
     * @param valueResolver 字符串处理器
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * 获取当前BeanFactory中所有嵌入式字符串处理器
     */
    StringValueResolver[] getEmbeddedValueResolvers();
}
