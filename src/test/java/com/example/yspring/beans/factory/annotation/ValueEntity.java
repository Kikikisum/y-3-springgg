package com.example.yspring.beans.factory.annotation;

import com.example.y3spring.beans.factory.annotation.Value;
import com.example.y3spring.context.annotation.Component;

@Component
public class ValueEntity {
    @Value("${kikikisum}")
    private String testString;

    @Value("666")
    private double testDouble;

    public ValueEntity(){

    }

    public void hello(){
        System.out.println(testString);
        System.out.println(testDouble);
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public void setTestDouble(double testDouble) {
        this.testDouble = testDouble;
    }

    public String getTestString() {
        return testString;
    }

    public double getTestDouble() {
        return testDouble;
    }
}
