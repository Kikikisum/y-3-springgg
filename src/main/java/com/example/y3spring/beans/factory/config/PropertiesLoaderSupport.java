package com.example.y3spring.beans.factory.config;

import com.example.y3spring.beans.factory.co.io.Resource;
import com.example.y3spring.beans.factory.co.io.ResourceLoader;
import com.example.y3spring.beans.factory.exception.NestedIOException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 提供了加载本地上下文所有property文件功能的基类
 */
public class PropertiesLoaderSupport {

    private ResourceLoader resourceLoader;

    private String[] locations;

    public PropertiesLoaderSupport(){}

    public PropertiesLoaderSupport(String...locations){
        this.locations = locations;
    }

    /**
     * 从上下文中加载所有的properties
     * @return 上下文中所有properties的集合
     */
    protected Properties loadProperties() throws NestedIOException {

        Properties properties = new Properties();

        // 如果没有指定的路径 则自动扫描
        if(this.locations == null){
            this.locations = autoScanProperties();
        }

        for (String location : this.locations) {
            Resource resource = resourceLoader.getResource(location);
            try {
                properties.load(resource.getInputStream());
            } catch (IOException e) {
                throw new NestedIOException("load properties [" + resource.getFileName() + "] error");
            }
        }

        return properties;
    }

    /**
     * 自动扫描类路径下的properties文件路径
     */
    protected String[] autoScanProperties(){
        List<String> locationList = new ArrayList<>();
        File file = new File("src/main/resources");
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files != null){
                for (File listFile : files) {
                    if(listFile.getName().endsWith(".properties")){
                        locationList.add(listFile.toURI().toString());
                    }
                }
            }
        }
        return locationList.toArray(new String[0]);
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setLocations(String...locations) {
        this.locations = locations;
    }
}
