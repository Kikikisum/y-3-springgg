package com.example.y3spring.context.event;

import com.example.y3spring.context.ApplicationContext;
import com.example.y3spring.context.ApplicationEvent;

/**
 * Spring上下文中事件的基类
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    /**
     * 注册该事件的上下文对象
     */
    protected final ApplicationContext applicationContext;

    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
        this.applicationContext = source;
    }
}
