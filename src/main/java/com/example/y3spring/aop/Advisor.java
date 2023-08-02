package com.example.y3spring.aop;

import org.aopalliance.aop.Advice;

public interface Advisor {
    Advice EMPTY_INSTANCE = new Advice() {};

    /**
     * 若配置了通知则返回通知对象，否则返回空白对象
     */
    Advice getAdvice();
}
