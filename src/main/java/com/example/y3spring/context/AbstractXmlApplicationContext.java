package com.example.y3spring.context;

import com.example.y3spring.co.io.DefaultResourceLoader;
import com.example.y3spring.co.io.Resource;
import com.example.y3spring.context.beans.factory.support.DefaultListableBeanFactory;
import com.example.y3spring.context.beans.factory.support.XmlBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    /**
     * 使用xml配置文件的方式读取bean定义
     * @param beanFactory 要加载进的beanFactory
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(new DefaultResourceLoader(), beanFactory);

        beanDefinitionReader.loadBeanDefinitions(getConfigResources());

    }

    /**
     * 由子类实现
     */
    protected abstract Resource[] getConfigResources();


    protected Resource[] getResourcesByLocations(String[] locations){
        Resource[] resources = new Resource[locations.length];
        for (int i = 0; i < locations.length; i++) {
            resources[i] = getResource(locations[i]);
        }
        return resources;
    }
}
