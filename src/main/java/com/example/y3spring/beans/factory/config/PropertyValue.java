package com.example.y3spring.beans.factory.config;

public class PropertyValue {

    private  String name;
    private Object value;

    public PropertyValue(String name, Object value){
        this.name=name;
        this.value=value;
    }

    public String getName(){
        return name;
    }

    public Object getValue(){
        return value;
    }

    public void setPropertyValue(Object propertyValue) {
        value = propertyValue;
    }
}
