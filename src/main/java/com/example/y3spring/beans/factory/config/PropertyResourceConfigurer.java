package com.example.y3spring.beans.factory.config;

import com.example.y3spring.beans.factory.ConfigurableListableBeanFactory;
import com.example.y3spring.beans.factory.exception.NestedIOException;

import java.util.Properties;

/**
 * 该抽象基类提供了能够根据Properties文件来改变bean定义中相应属性的功能
 */
public abstract class PropertyResourceConfigurer extends PropertiesLoaderSupport implements BeanFactoryPostProcessor{
    /**
     * 模板方法 具体逻辑子类实现
     * @param beanFactory 上下文中所使用的工厂对象
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory){
        // 1、读取上下文的property
        Properties props = null;
        try {
            props = loadProperties();
        } catch (NestedIOException e) {
            System.out.println("加载上下文配置文件时出错");
        }
        // 2、将property应用于BeanFactory
        processProperties(beanFactory,props);
    }


    /**
     * 将给定的property中的属性应用于BeanFactory中的bean定义
     * @param beanFactory 指定的BeanFactory
     * @param props property属性集合
     */
    protected abstract void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props);
}
