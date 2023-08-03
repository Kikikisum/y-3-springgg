package com.example.y3spring.context.event;

import com.example.y3spring.context.ApplicationContext;

/**
 * 在初始化或者刷新ApplicationContext时触发的事件
 */
public class ContextRefreshedEvent extends ApplicationContextEvent{

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }
}
