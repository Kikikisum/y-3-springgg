package com.example.y3spring.context.beans;

import com.example.y3spring.context.beans.config.PropertyValue;
import com.example.y3spring.context.beans.config.PropertyValues;
import com.example.y3spring.context.beans.support.InstantiationStrategy;
import com.example.y3spring.context.beans.support.SimpleInstantiationStrategy;
import com.example.y3spring.context.beans.utils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

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

        // 获取该Bean类的所有PropertyDescriptor
        Map<String, PropertyDescriptor> beanPropertyMap = PropertyUtils.getBeanPropertyMap(clazz);

        for(PropertyValue pv : propertyValues.getPropertyValues()){
            String propertyName = pv.getName();
            Object propertyValue = pv.getValue();
            try {
                // todo xml配置中 普通属性不能配置类型 应该以其他方式获取
                PropertyDescriptor pd = beanPropertyMap.get(propertyName);

                Method setterMethod = pd.getWriteMethod();
                setterMethod.invoke(bean, propertyValue);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
