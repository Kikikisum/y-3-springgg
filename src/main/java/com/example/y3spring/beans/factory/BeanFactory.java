package com.example.y3spring.beans.factory;

public interface BeanFactory {
    /**
     * 获取bean
     * @param beanName bean的名字
     * @return bean的实例对象
     */
    Object getBean(String beanName);

    /**
     * 根据指定类型获取bean（避免强转的方法）
     * @param beanName bean的名字
     * @param requiredType 需要的类型
     */
    Object getBean(String beanName,Class<?> requiredType);
}
