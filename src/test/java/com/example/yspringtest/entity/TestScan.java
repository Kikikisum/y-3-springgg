package com.example.yspringtest.entity;

import com.example.y3spring.context.annotation.Component;

@Component("666")
public class TestScan {

    public void test(){
        System.out.println("测试测试注释@Component");
    }

}
