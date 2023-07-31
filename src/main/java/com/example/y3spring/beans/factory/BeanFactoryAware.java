package com.example.y3spring.beans.factory;

import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.context.Aware;

public interface BeanFactoryAware extends Aware {
    void setBeanFactory(BeanFactory beanFactory);
}
