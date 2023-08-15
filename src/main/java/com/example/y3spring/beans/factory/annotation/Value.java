package com.example.y3spring.beans.factory.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * 用于标注属性，两种格式 ： 1、{value} 直接注入属性 2、${propertyName} 根据配置文件注入
     */
    String value() default "";
}