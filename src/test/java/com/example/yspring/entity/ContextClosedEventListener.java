package com.example.yspring.entity;

import com.example.y3spring.context.ApplicationListener;
import com.example.y3spring.context.event.ContextClosedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("上下文:{}已关闭",event);
    }
}
