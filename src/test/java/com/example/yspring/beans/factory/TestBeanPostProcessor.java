package com.example.yspring.beans.factory;

import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.yspring.entity.Car;

public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public <T> T postProcessBeforeInitialization(T bean, String beanName) {
        System.out.println("初始化之前被调用");
        System.out.println(bean);
        if(bean instanceof Car){
            ((Car) bean).setBrand("我变成厉害了！");
        }
        return bean;
    }

    @Override
    public <T> T postProcessAfterInitialization(T bean, String beanName) {
        System.out.println("初始化之后被调用");
        System.out.println(bean);
        if(bean instanceof Car){
            Car car = (Car) bean;
            System.out.println("我是一台" + car.getBrand());
        }
        return bean;
    }
}
