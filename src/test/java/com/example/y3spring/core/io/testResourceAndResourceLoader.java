package com.example.y3spring.core.io;

import cn.hutool.core.io.IoUtil;
import com.example.y3spring.beans.factory.co.io.DefaultResourceLoader;
import com.example.y3spring.beans.factory.co.io.Resource;
import com.example.y3spring.beans.factory.co.io.ResourceLoader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class testResourceAndResourceLoader {
    /**
     * 测试DefaultResourceLoader
     */
    @Test
    public void testDefaultResourceLoader() throws FileNotFoundException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        // 测试加载classpath下的资源
        Resource resource = resourceLoader.getResource("classpath:testResouceLoader.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(resource);
        System.out.println(content);
        System.out.println("-------------");

        // 测试加载文件系统的资源
        // 相对路径
        resource = resourceLoader.getResource("test/java/com/example/resources/testResouceLoader.txt");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(resource);
        System.out.println(content);
        System.out.println("-------------");
        // 绝对路径
        resource = resourceLoader.getResource("\"C:\\Users\\86188\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo\\y-3-spring\\src\\test\\java\\com\\example\\y3spring\\resources\\testResouceLoader.txt");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(resource);
        System.out.println(content);
        System.out.println("-------------");

        // 测试加载基于URL的资源
        resource = resourceLoader.getResource("https://www.baidu.com");
        inputStream = resource.getInputStream();
        System.out.println(resource);
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        System.out.println("-------------");
    }
}
