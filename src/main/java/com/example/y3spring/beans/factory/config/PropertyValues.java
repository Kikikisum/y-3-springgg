package com.example.y3spring.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    public void addPropertyValue(PropertyValue pv){
        if(pv == null){
            throw new IllegalArgumentException("初始化bean添加的属性不能为空");
        }
        for (int i = 0; i < propertyValueList.size(); i++) {
            if(propertyValueList.get(i).getValue().equals(pv.getName())){
                // 覆盖已有的属性值
                propertyValueList.set(i,pv);
                return;
            }
        }
        // 否则就添加
        propertyValueList.add(pv);
    }

    public PropertyValue[] getPropertyValues(){
        return propertyValueList.toArray(new PropertyValue[0]);
    }
}
