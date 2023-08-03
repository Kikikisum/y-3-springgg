package com.example.y3spring.context;

/**
 * Spring事件机制中的发布者，提供了发布事件的接口
 */
public interface ApplicationEventPublisher {

    /**
     * 发布事件 最终发送到广播者处
     */
    void publishEvent(ApplicationEvent event);
}
