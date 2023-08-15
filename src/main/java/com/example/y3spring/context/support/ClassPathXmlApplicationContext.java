package com.example.y3spring.context.support;

import com.example.y3spring.beans.factory.co.io.Resource;

import java.util.Properties;

/**
 * 该子类是提供给用户使用的实现类，可按照给定的类路径自动加载bean
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{

    private final Resource[] configResources;

    public ClassPathXmlApplicationContext(String configLocation){
        this(new String[]{configLocation},true);
    }
    public ClassPathXmlApplicationContext(String...configLocations) {
        this(configLocations,true);
    }

    /**
     * @param refresh 是否启动上下文
     */
    public ClassPathXmlApplicationContext(String[] configLocations,boolean refresh){
        configResources = getResourcesByLocations(configLocations);
        if(refresh){
            refresh();
        }
    }

    @Override
    protected Resource[] getConfigResources() {
        return configResources;
    }

    @Override
    public Properties getConfig() {
        return null;
    }
}
