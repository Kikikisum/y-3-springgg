package com.example.y3spring.beans.factory.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

/**
 * 从类路径获取资源
 */

public class ClassPathResource extends AbstractResource {
    private final String path;
    private final ClassLoader classLoader;
    @Override
    public InputStream getInputStream() throws FileNotFoundException {

        InputStream inputStream = classLoader.getResourceAsStream(path);
        if(inputStream == null){
            throw new FileNotFoundException("类路径下没有找到该资源: " + path);
        }
        return inputStream;
    }

    @Override
    public URL getURL() throws FileNotFoundException {
        URL url = classLoader.getResource(path);
        if(url == null){
            throw new FileNotFoundException("类路径下没有找到该资源: " + path);
        }
        return url;
    }

    @Override
    public String getFileName() {
        return classLoader.getResource(path).getFile();
    }

    public ClassPathResource(String path) {
        this.path = path;
        classLoader = this.getClass().getClassLoader();
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = classLoader;
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this.path = path;

        classLoader = clazz.getClassLoader();
    }

}
