package com.example.y3spring.context.support;

import com.example.y3spring.beans.factory.co.io.Resource;

import java.util.Properties;

public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {
    private final Resource[] resources;

    public FileSystemXmlApplicationContext(String configLocation){
        this(new String[]{configLocation},true);
    }
    public FileSystemXmlApplicationContext(String...configLocations){
        this(configLocations,true);
    }
    public FileSystemXmlApplicationContext(String[] configLocations,boolean refresh){
        resources = getResourcesByLocations(configLocations);

        if(refresh){
            refresh();
        }
    }

    @Override
    protected Resource[] getConfigResources() {
        return resources;
    }

    /**
     * @return
     */
    @Override
    public Properties getConfig() {
        return null;
    }
}
