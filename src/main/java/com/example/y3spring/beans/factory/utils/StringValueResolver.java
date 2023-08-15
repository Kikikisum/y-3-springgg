package com.example.y3spring.beans.factory.utils;

public interface StringValueResolver {

    /**
     * 按照一定规则解析给定的字符串
     * @param str 需要被解析的字符串
     * @return 解析后的字符串
     */
    String resolveStringValue(String str);
}
