package com.example.y3spring.annotation;
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YRequestParam {
    String value() default "";

    boolean required() default true;
}