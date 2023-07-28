package com.example.y3spring.context.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{
    /**
     * bean定义的注册表
     */
    private static final Map<String,BeanDefinition<?>> BEAN_DEFINITION_REGISTRY = new HashMap<>();

    @Override
    protected BeanDefinition<?> getBeanDefinition(String beanName) {
        if(beanName==null){
            throw new IllegalArgumentException("beanName不能为空");
        }

        BeanDefinition<?> beanDefinition = BEAN_DEFINITION_REGISTRY.get(beanName);

        if(beanDefinition == null){
            throw new IllegalArgumentException("不存在该beanName的bean定义");
        }

        return beanDefinition;
    }

    @Override
    public <T> void registerBeanDefinition(String beanName, BeanDefinition<T> beanDefinition) {
        if(beanName==null){
            throw new IllegalArgumentException("beanName不能为空");
        }
        BEAN_DEFINITION_REGISTRY.put(beanName,beanDefinition);
    }

    /*
    @Override
    public <T> T doCreate(String beanName, BeanDefinition<T> beanDefinition) {
        String name = beanDefinition.getName();
        if(!name.equals(beanName)){
            throw new IllegalArgumentException("BeanName和BeanDefinition中的名字不对应，创建bean实例失败");
        }
        Class<T> clazz = beanDefinition.getType();
        Class<?>[] parameterTypes = beanDefinition.getParameterTypes();
        Object[] parameterValues = beanDefinition.getValues();

        T beanInstance = null;
        try {
            Constructor<T> constructor = clazz.getConstructor(parameterTypes);

            beanInstance = constructor.newInstance(parameterValues);

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
        }

        return beanInstance;
    }
    */
}
