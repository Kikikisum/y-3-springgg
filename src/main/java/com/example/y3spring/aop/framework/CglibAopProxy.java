package com.example.y3spring.aop.framework;

public class CglibAopProxy implements AopProxy{
    private final AdvisedSupport advisedSupport;

    public CglibAopProxy(AdvisedSupport config){
        this.advisedSupport = config;
    }
    @Override
    public Object getProxy() {
        return null;
    }
}
