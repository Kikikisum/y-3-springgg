package com.example.y3spring.beans.factory.utils;

import java.util.Properties;

public class PlaceholderResolvingStringValueResolver implements StringValueResolver{

    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";

    private Properties properties;

    public PlaceholderResolvingStringValueResolver(Properties properties){
        this.properties = properties;
    }

    @Override
    public String resolveStringValue(String str){
        // 如果属性值是占位符 则替换
        if(str.startsWith(PLACEHOLDER_PREFIX) && str.endsWith(PLACEHOLDER_SUFFIX)){
            int length = str.length();
            str = str.substring(PLACEHOLDER_PREFIX.length(),length-PLACEHOLDER_SUFFIX.length());

            // 作为key从props中获取属性
            str = properties.getProperty(str);
        }
        return str;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
