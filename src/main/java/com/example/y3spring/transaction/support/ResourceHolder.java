package com.example.y3spring.transaction.support;

public interface ResourceHolder {
    /**
     * 重置连接
     */
    void reset();

    /**
     * 是否有连接
     */
    void unbound();

    /**
     * 是否存在先前线程剩余的部分
     * @return
     */
    boolean isVoid();
}
