package com.example.y3spring.beans.factory.support;

import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.context.ApplicationContextAware;
import com.example.y3spring.context.ConfigurableApplicationContext;

/**
 * ApplicationContextAware接口的bean回调注入ApplicationContext的BeanPostProcessor
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    private final ConfigurableApplicationContext applicationContext;


    public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T postProcessBeforeInitialization(T bean, String beanName) {
        if(bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }

        return bean;
    }
}
