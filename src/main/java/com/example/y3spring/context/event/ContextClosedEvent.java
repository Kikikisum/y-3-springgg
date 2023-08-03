package com.example.y3spring.context.event;

import com.example.y3spring.context.ApplicationContext;

/**
 * ApplicationContext被关闭时触发的事件
 */
public class ContextClosedEvent extends ApplicationContextEvent{

    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }
}
