package com.example.yspring.entity;

import com.example.y3spring.beans.factory.DisposableBean;
import lombok.Data;

@Data
public class TestDestroy implements DisposableBean {

    private String name;

    private Integer age;

    @Override
    public void destroy() {
        System.out.println("正在执行DisposableBean接口的destroy方法");
        System.out.println(name);
    }

    public void myDestroy(){
        System.out.println("正在执行指定的destroy方法");
        System.out.println(name);
    }
}
