package com.example.y3spring.beans.factory.io;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * 使用策略模式获取Resource的实现类
 */

public class DefaultResourceLoader implements ResourceLoader{
    private static final String CLASSPATH_PREFIX = "classpath:";

    @Override
    public Resource getResource(String location) {
        // 如果以classpath开头，则返回ClassPathResource
        if(location.startsWith(CLASSPATH_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_PREFIX.length()));
        }else {
            // 否则先尝试转换成url
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                // url格式错误，再尝试使用file体系
                return new FileSystemResource(location);
            }
        }
    }
}
