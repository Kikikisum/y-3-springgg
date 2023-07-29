package com.example.y3spring.context.beans.factory.config;
import com.example.y3spring.context.beans.factory.config.PropertyValues;
import lombok.Data;

@Data
public class BeanDefinition<T> {
    /**
     * Bean的类型
     */
    private Class<T> type;
    /**
     * Bean的属性
     */
    private PropertyValues propertyValues;
    /**
     * 指定的初始化方法名字
     */
    private String initMethodName;
    /**
     * 指定的destroy方法名字
     */
    private String destroyMethodName;


    public BeanDefinition(){};

    public BeanDefinition(Class<T> type, PropertyValues propertyValues) {
        this.type = type;
        this.propertyValues = propertyValues;
    }

    public BeanDefinition(Class<T> type, PropertyValues propertyValues, String initMethodName, String destroyMethodName) {
        this.type = type;
        this.propertyValues = propertyValues;
        this.initMethodName = initMethodName;
        this.destroyMethodName = destroyMethodName;
    }
}