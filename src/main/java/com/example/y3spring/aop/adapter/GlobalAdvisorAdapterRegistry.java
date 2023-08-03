package com.example.y3spring.aop.adapter;

/**
 * 全局通用的AdvisorAdapter注册表，用于获取单例的注册表
 */
public class GlobalAdvisorAdapterRegistry {

    private static AdvisorAdapterRegistry instance = new DefaultAdvisorAdapterRegistry();

    public static AdvisorAdapterRegistry getInstance(){
        return instance;
    }
}
