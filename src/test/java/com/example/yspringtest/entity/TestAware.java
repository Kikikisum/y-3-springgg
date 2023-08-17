package com.example.yspringtest.entity;

import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.beans.factory.BeanFactoryAware;
import com.example.y3spring.context.ApplicationContext;
import com.example.y3spring.context.ApplicationContextAware;
import lombok.Data;

@Data
public class TestAware implements BeanFactoryAware, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
