package com.example.y3spring.aop;

/**
 * 用于封装和获取将要被Aop的目标对象
 */
public interface TargetSource {
    /**
     * 在Aop通知的逻辑执行之前调用，获取被Aop的目标对象
     * @return 连接点所在的目标对象，若不存在则返回null
     */
    Object getTarget();
}
