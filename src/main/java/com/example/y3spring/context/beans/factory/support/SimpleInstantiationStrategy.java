package com.example.y3spring.context.beans.factory.support;

import com.example.y3spring.context.beans.factory.BeanDefinition;
import com.example.y3spring.context.beans.factory.config.InstantiationStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiationStrategy implements InstantiationStrategy {
    @Override
    public <T> T instantiation(BeanDefinition<T> beanDefinition) {
        Class<T> clazz = beanDefinition.getType();

        T instance = null;
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();

            instance = constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return instance;
    }
}
