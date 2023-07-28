package com.example.y3spring.context.beans;
import com.example.y3spring.context.beans.config.PropertyValues;
import lombok.Data;

@Data
public class BeanDefinition<T> {
    /**
     * Bean的名字
     */
    private String name;
    /**
     * Bean的类型
     */
    private Class<T> type;
    /**
     * Bean的属性
     */
    private PropertyValues propertyValues;

    public BeanDefinition(String name,Class<T> type){
        this.name = name;
        this.type = type;
    }
}