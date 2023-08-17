package com.example.yspringtest.aop;

public class TestService {

    public String testPointCut(){
        String str = "测试是否能够匹配到切入点";
        System.out.println(str);

        return str;
    }

    public String test2(int niu){
        niu = 666;

        return String.valueOf(niu);
    }

}
