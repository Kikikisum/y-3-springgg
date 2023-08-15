package com.example.y3spring.beans.factory.support;

import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.context.ApplicationContext;
import com.example.y3spring.context.ApplicationContextAware;
import com.example.y3spring.context.ApplicationListener;
import com.example.y3spring.context.ConfigurableApplicationContext;

public class ApplicationListenerDetector implements BeanPostProcessor, ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public <T> T postProcessAfterInitialization(T bean, String beanName) {
        // 检查是否是监听器
        if(bean instanceof ApplicationListener){
            //若是 则注册进上下文中
            applicationContext.addApplicationListener((ApplicationListener<?>) bean);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }
}
