package com.example.y3spring.context;

import java.util.EventObject;

/**
 * Spring中事件的顶层抽象类
 */

public abstract class ApplicationEvent extends EventObject {
    public ApplicationEvent(Object source) {
        super(source);
    }
}
