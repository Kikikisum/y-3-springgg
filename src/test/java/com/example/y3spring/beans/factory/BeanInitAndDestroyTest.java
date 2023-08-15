package com.example.y3spring.beans.factory;

import com.example.y3spring.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class BeanInitAndDestroyTest {

    @Test
    public void testInitMethod(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testInit.xml");
    }

    @Test
    public void testDestroyMethod(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testDestroy.xml");
        classPathXmlApplicationContext.close();

    }
}
