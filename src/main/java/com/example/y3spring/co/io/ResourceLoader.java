package com.example.y3spring.co.io;

import com.example.y3spring.co.io.Resource;

public interface ResourceLoader {
    /**
     * 根据路径加载资源
     * @param location 资源路径
     * @return
     */
    Resource getResource(String location);
}
