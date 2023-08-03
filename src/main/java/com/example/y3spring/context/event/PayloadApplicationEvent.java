package com.example.y3spring.context.event;

import com.example.y3spring.context.ApplicationEvent;

public class PayloadApplicationEvent<T> extends ApplicationEvent {

    /**
     * 该事件的载荷
     */
    private final T payload;

    public PayloadApplicationEvent(T payload) {
        super(payload);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
