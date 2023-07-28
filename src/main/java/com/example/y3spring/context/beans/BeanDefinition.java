package com.example.y3spring.context.beans;
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
     * Bean所带的参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * Bean所带的属性值
     */
    private Object[] values;

    public BeanDefinition(String name,Class<T> type){
        this.name = name;
        this.type = type;
    }
}