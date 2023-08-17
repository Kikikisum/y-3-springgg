package com.example.yspringtest.beans.factory;

import com.example.y3spring.beans.factory.co.io.DefaultResourceLoader;
import com.example.y3spring.beans.factory.support.DefaultListableBeanFactory;
import com.example.y3spring.beans.factory.support.XmlBeanDefinitionReader;
import com.example.y3spring.context.support.ClassPathXmlApplicationContext;
import com.example.yspringtest.entity.TestAware;
import org.junit.Test;

public class AwareTest {

    @Test
    public void testBeanFactoryAware(){
        // 使用BeanFactory测试
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(new DefaultResourceLoader(), beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:testAware.xml");

        Object testAware = beanFactory.getBean("testAware");

        System.out.println(testAware);
        System.out.println(beanFactory);
        System.out.println(((TestAware)testAware).getBeanFactory());

    }

    @Test
    public void testApplicationAware(){
        // 使用ApplicationContext测试
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:testAware.xml");

        TestAware testAware = (TestAware) applicationContext.getBean("testAware");

        System.out.println(testAware);
        System.out.println(testAware.getApplicationContext());
        System.out.println(applicationContext);

    }
}
