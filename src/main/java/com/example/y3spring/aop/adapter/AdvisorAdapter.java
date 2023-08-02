package com.example.y3spring.aop.adapter;

import com.example.y3spring.aop.Advisor;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * 拓展用的适配器，实现了该接口的类可以使用自定义类型的Advice和Advisor
 */
public interface AdvisorAdapter {
    /**
     * 获取指定Advisor所适配的拦截器
     */
    MethodInterceptor getInterceptor(Advisor advisor);

    /**
     * 检查当前适配器是否支持目标类型的通知，能否为其创建一个相应的拦截器
     */
    boolean supportAdvice(Advice advice);
}
