package com.example.y3spring.context.beans;

public class SetValue {

    private final String name;
    private final Object value;

    public SetValue(String name,Object value){
        this.name=name;
        this.value=value;
    }

    public String getName(){
        return name;
    }

    public Object getValue(){
        return value;
    }

}
