package com.example.y3spring.context;

import com.example.y3spring.beans.factory.co.io.Resource;

public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext{
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
}
