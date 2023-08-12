package com.example.y3spring.annotation;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface YService {
    String value() default "";
}