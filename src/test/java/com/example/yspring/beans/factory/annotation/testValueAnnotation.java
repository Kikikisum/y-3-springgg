package com.example.yspring.beans.factory.annotation;

import com.example.y3spring.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * 测试@Value注解
 */
public class testValueAnnotation {

    @Test
    public void testSimpleInject(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testValue.xml");
        ValueEntity valueEntity = (ValueEntity) classPathXmlApplicationContext.getBean("valueEntity");

        valueEntity.hello();
    }

    @Test
    public void testXMLAndComponent(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testValue.xml");
        ValueEntity valueEntity = (ValueEntity) classPathXmlApplicationContext.getBean("valueEntity");
        valueEntity.hello();
        System.out.println(valueEntity);

        ValueEntity valueEntity2 = (ValueEntity) classPathXmlApplicationContext.getBean("valueEntity2");
        valueEntity2.hello();
        System.out.println(valueEntity2);
    }
}
