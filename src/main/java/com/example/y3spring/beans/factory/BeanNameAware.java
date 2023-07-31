package com.example.y3spring.beans.factory;

import com.example.y3spring.context.Aware;

public interface BeanNameAware extends Aware {

    void setBeanName(String beanName);
}
