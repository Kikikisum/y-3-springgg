package com.example.y3spring.context.beans.factory.support;

import com.example.y3spring.co.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{
    private final ResourceLoader resourceLoader;

    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public AbstractBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.resourceLoader = resourceLoader;
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    @Override
    public void loadBeanDefinitions(String location) {
        loadBeanDefinitions(resourceLoader.getResource(location));
    }

    @Override
    public void loadBeanDefinitions(String[] locations) {
        for (String location : locations) {
            loadBeanDefinitions(resourceLoader.getResource(location));
        }
    }
}
