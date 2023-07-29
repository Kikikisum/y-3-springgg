package com.example.y3spring.context.beans.factory.support;

import com.example.y3spring.co.io.Resource;
import com.example.y3spring.co.io.ResourceLoader;

/**
 * 提供读取Bean定义的接口
 */
public interface BeanDefinitionReader {
    ResourceLoader getResourceLoader();

    BeanDefinitionRegistry getBeanDefinitionRegistry();

    void loadBeanDefinitions(String location);

    void loadBeanDefinitions(String[] locations);

    void loadBeanDefinitions(Resource resource);
    void loadBeanDefinitions(Resource[] resources);
}
