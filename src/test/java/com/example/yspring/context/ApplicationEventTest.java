package com.example.yspring.context;

import com.example.y3spring.context.support.ClassPathXmlApplicationContext;
import com.example.yspring.entity.Car;
import com.example.yspring.entity.CarEvent;
import com.example.yspring.entity.CarEventListener;
import org.junit.Test;

import java.util.concurrent.Executors;

public class ApplicationEventTest {
    /**
     * 测试ApplicationContext内部自动发布的事件
     */
    @Test
    public void testAutoEvent() throws InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testEvent.xml");

        Thread.sleep(2000);

        classPathXmlApplicationContext.close();
    }

    @Test
    public void testManualEvent() throws InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
        CarEvent playCarEvent = new CarEvent(new Car("宝马", 200000, "666"));
        CarEventListener playCarEventListener = new CarEventListener();

        classPathXmlApplicationContext.addApplicationListener(playCarEventListener);
        System.out.println("监听器已添加");

        Thread.sleep(2000);

        classPathXmlApplicationContext.publishEvent(playCarEvent);
        System.out.println("事件已发布");
    }

    /**
     * 测试异步处理事件
     */
    @Test
    public void testExecutorEvent() throws InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
        classPathXmlApplicationContext.addTaskExecutor(Executors.newSingleThreadExecutor());

        CarEvent playCarEvent = new CarEvent(new Car("宝马", 200000, "666"));
        CarEventListener playCarEventListener = new CarEventListener();

        classPathXmlApplicationContext.addApplicationListener(playCarEventListener);
        System.out.println("监听器已添加");

        Thread.sleep(2000);

        classPathXmlApplicationContext.publishEvent(playCarEvent);
        System.out.println("事件已发布");

        Thread.sleep(4000);
    }
}
