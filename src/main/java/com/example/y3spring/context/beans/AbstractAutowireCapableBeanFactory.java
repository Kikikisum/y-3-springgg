package com.example.y3spring.context.beans;

import com.example.y3spring.support.InstantiationStrategy;
import com.example.y3spring.support.SimpleInstantiationStrategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
    private static final InstantiationStrategy INSTANTIATION_STRATEGY = new SimpleInstantiationStrategy();

    @Override
    protected <T> T createBean(String beanName, BeanDefinition<T> beanDefinition) {
        return doCreateBean(beanName,beanDefinition);
    }

    /**
     * 创建Bean实例的实际逻辑
     */
    public <T> T doCreateBean(String beanName, BeanDefinition<T> beanDefinition){
        String name = beanDefinition.getName();
        if(!name.equals(beanName)){
            throw new IllegalArgumentException("BeanName和BeanDefinition中的名字不对应，创建bean实例失败");
        }
        Class<T> clazz = beanDefinition.getType();

        T beanInstance = INSTANTIATION_STRATEGY.instantiation(beanDefinition);
        autoWirePropertyValues(beanInstance,beanDefinition);

        return beanInstance;
    }

    /**
     * 为bean实例对象自动装配属性 (底层使用setter注入）
     * @param bean 实例对象
     * @param beanDefinition bean定义
     */
    public <T> void autoWirePropertyValues(T bean, BeanDefinition<T> beanDefinition){
        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        Class<?> clazz = bean.getClass();
        for(PropertyValue pv : propertyValues.getPropertyValues()){

            String propertyName = pv.getName();
            Object propertyValue = pv.getValue();

            try {
                Class<?> propertyType = clazz.getDeclaredField(propertyName).getType();
                // 使用setter注入 要获取setter方法
                // setXxxx()
                String methodName = "set" + propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);
                Method setterMethod = clazz.getDeclaredMethod(methodName, propertyType);

                setterMethod.invoke(bean,propertyValue);

            } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
