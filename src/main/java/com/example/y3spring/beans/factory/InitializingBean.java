package com.example.y3spring.beans.factory;

/**
 * 在beanFactory为bean装配完属性后调用
 */
public interface InitializingBean {

    void afterPropertiesSet();
}
