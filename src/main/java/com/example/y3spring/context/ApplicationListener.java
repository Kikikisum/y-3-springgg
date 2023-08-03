package com.example.y3spring.context;

import java.util.EventListener;

/**
 *  Spring事件机制中的监听者，可以与广播者相绑定，监听目标广播者上的指定类型事件
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 处理指定类型事件的函数式接口
     */
    void onApplicationEvent(E event);
}
