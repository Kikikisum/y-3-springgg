package com.example.yspring.entity;

import com.example.y3spring.beans.factory.FactoryBean;

public class CarFactory implements FactoryBean<Car> {

    @Override
    public Car getObject() {
        return new Car("宝马",200000,"666");
    }

    @Override
    public Class<Car> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
