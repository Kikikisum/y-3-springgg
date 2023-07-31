package com.example.y3spring.context;

/**
 * Bean通过该接口通过回调感知所属的ApplicationContext
 */
public interface ApplicationContextAware extends Aware{
    /**
     * 回调函数
     * @param applicationContext
     */
    void setApplicationContext(ApplicationContext applicationContext);
}
