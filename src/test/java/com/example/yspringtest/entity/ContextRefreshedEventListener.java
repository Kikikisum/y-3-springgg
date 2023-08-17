package com.example.yspringtest.entity;

import com.example.y3spring.context.ApplicationListener;
import com.example.y3spring.context.event.ContextRefreshedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("上下文:{}刷新完毕",event);
    }
}
