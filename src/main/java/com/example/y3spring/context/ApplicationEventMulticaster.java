package com.example.y3spring.context;

import java.util.concurrent.Executor;

/**
 * Spring事件机制中的广播者，提供将事件转发给事件监听者，让监听者和事件类型相绑定的接口
 */

public interface ApplicationEventMulticaster {

    /**
     * 添加绑定listener
     *
     * @param applicationListener 要添加的listener
     */
    void addApplicationListener(ApplicationListener<?> applicationListener);

    /**
     * 卸载指定listener
     *
     * @param applicationListener 要删除的listener
     */
    void removeApplicationListener(ApplicationListener<?> applicationListener);

    /**
     * 添加applicationListenerBean，根据beanName从容器中取出再添加
     *
     * @param beanName 指定的listener在容器中的名字
     */
    void addApplicationListenerBean(String beanName);

    /**
     * 根据beanName卸载指定listenerBean
     *
     * @param beanName 要卸载的listenBean的名字
     */
    void removeApplicationListenerBean(String beanName);

    /**
     * 广播指定的事件
     *
     * @param event 需要广播的事件
     */
    void multicastEvent(ApplicationEvent event);

    /**
     * 为当前的广播器添加异步线程池
     *
     * @param executor 要添加的线程池
     */
    void addTaskExecutor(Executor executor);
}

