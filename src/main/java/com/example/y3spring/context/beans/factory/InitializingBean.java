package com.example.y3spring.context.beans.factory;

/**
 * 在beanFactory为bean装配完属性后调用，可以自定义
 */
public interface InitializingBean {

    void afterPropertiesSet();
}
