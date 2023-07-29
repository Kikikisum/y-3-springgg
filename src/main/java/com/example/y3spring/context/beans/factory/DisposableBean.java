package com.example.y3spring.context.beans.factory;

/**
 * 在bean销毁时，由其所属的BeanFactory调用
 */
public interface DisposableBean {

    void destroy();
}
