package com.example.yspring.entity;

import com.example.y3spring.beans.factory.InitializingBean;
import lombok.Data;

@Data
public class TestInit implements InitializingBean {

    private String name;

    private int age;

    public TestInit(){
        System.out.println("执行了构造方法");
    }

    public void init(){
        System.out.println("执行了指定的init方法");
        System.out.println("我是"+name);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("执行了afterPropertiesSet方法");
        System.out.println("我是"+name);
    }
}
