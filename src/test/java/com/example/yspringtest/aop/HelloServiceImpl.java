package com.example.yspringtest.aop;

public class HelloServiceImpl implements HelloService{
    @Override
    public void hello() {
        System.out.println("未被动态代理前的方法");
    }
}
