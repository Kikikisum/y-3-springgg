package com.example.y3spring.aop.adapter;

import com.example.y3spring.aop.Advisor;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * AdvisorAdapter注册中心的接口
 */
public interface AdvisorAdapterRegistry {
    /**
     * 向注册表注册目标AdvisorAdapter
     * @param adapter 要注册的adapter
     */
    void registerAdvisorAdapter(AdvisorAdapter adapter);

    /**
     * 在注册中心中获取支持目标advisor的拦截器列表
     * @param advisor 要查询的advisor
     */
    MethodInterceptor[] getInterceptors(Advisor advisor);
}
