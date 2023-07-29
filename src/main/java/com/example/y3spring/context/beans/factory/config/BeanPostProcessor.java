package com.example.y3spring.context.beans.factory.config;

/**
 * Bean的后置处理器
 */
public interface BeanPostProcessor {
    /**
     * 在初始化bean之前被调用
     * @param bean 将被初始化的bean对象
     * @param name bean名字
     * @return 装饰后的bean对象
     */
    <T> T postProcessBeforeInitialization(T bean, String name);

    /**
     * 在初始化bean之后被调用
     * @param bean 被初始化的bean对象
     * @param name bean名字
     * @return 装饰后的bean对象
     */
    <T> T postProcessAfterInitialization(T bean, String name);
}
