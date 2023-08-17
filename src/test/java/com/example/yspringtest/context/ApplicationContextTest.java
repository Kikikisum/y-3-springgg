package com.example.yspringtest.context;

import com.example.y3spring.context.support.ClassPathXmlApplicationContext;
import com.example.y3spring.context.support.FileSystemXmlApplicationContext;
import org.junit.Test;

public class ApplicationContextTest {
    /**
     * 测试ClassPathXmlApplicationContext
     */
    @Test
    public void testClassPathXmlApplicationContext(){
        ClassPathXmlApplicationContext xmlApplicationContext1 = new ClassPathXmlApplicationContext("classpath:testBean.xml");
        Object carBean1 = xmlApplicationContext1.getBean("carBean1");
        System.out.println(carBean1);

        Object carRoll1 = xmlApplicationContext1.getBean("carRoll1");
        System.out.println(carRoll1);

    }

    /**
     * 测试FileSystemXmlApplicationContext
     */
    @Test
    public void testFileSystemXmlApplicationContext(){
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext = new FileSystemXmlApplicationContext("C:\\Users\\86188\\IdeaProjects\\demo3\\src\\main\\java\\com\\example\\demo\\y-3-spring\\src\\test\\java\\com\\example\\y3spring\\beans\\factory\\AwareTest.java");
        Object carBean1 = fileSystemXmlApplicationContext.getBean("carBean1");
        System.out.println(carBean1);

    }
}
