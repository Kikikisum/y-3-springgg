package com.example.yspringtest.aop.autoproxy;

import com.example.yspringtest.aop.HelloService;
import com.example.y3spring.context.ApplicationContext;
import com.example.y3spring.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;


public class TestAutoProxy {

    @Test
    public void test1(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:testAutoProxy.xml");

        Object object = applicationContext.getBean("helloService", HelloService.class);

        HelloService helloService = (HelloService) object;
        helloService.hello();
    }
}
