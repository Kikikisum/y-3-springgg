package com.example.yspring.beans.factory;

import com.example.y3spring.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class FactoryBeanTest {

    @Test
    public void testFactoryBeanPrototype(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testFactoryBeanPrototype.xml");

        Object carBeanFactoryPrototype1 = classPathXmlApplicationContext.getBean("carBeanFactoryPrototype");
        System.out.println(carBeanFactoryPrototype1);

        Object carBeanFactoryPrototype2 = classPathXmlApplicationContext.getBean("carBeanFactoryPrototype");
        System.out.println(carBeanFactoryPrototype2);
    }
}
