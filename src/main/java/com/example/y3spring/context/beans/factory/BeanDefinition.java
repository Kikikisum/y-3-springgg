package com.example.y3spring.context.beans.factory;
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
}