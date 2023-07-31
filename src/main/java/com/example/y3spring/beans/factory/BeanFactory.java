package com.example.y3spring.beans.factory;

public interface BeanFactory {
    /**
     * 获取bean
     * @param beanName bean的名字
     * @return bean的实例对象
     */
    Object getBean(String beanName);
}
