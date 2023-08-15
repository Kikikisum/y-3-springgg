package com.example.y3spring.aop;

public class HelloServiceImpl implements HelloService{
    @Override
    public void hello() {
        System.out.println("未被动态代理前的方法");
    }
}
