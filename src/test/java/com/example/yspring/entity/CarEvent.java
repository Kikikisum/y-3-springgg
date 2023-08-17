package com.example.yspring.entity;

import com.example.y3spring.context.event.PayloadApplicationEvent;

public class CarEvent extends PayloadApplicationEvent<Car> {

    public CarEvent(Car payload) {
        super(payload);
    }
}
