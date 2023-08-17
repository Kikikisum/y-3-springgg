package com.example.yspringtest.entity;

import com.example.y3spring.context.ApplicationListener;

public class CarEventListener implements ApplicationListener<CarEvent> {

    @Override
    public void onApplicationEvent(CarEvent event) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("在玩车");
        System.out.println(event.getPayload());

        System.out.println("事件已完成");
    }
}
