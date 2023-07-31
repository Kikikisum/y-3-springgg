package com.example.y3spring.beans.factory.co.io;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * 资源的抽象，一个Resource代表一个资源
 */

public interface Resource {
    /**
     * 判断资源是否存在
     */
    boolean isExist();
    /**
     * 获取资源的二进制流
     */
    InputStream getInputStream() throws FileNotFoundException;

    /**
     * 获取资源的URL
     */
    URL getURL() throws FileNotFoundException;

    /**
     * 获取资源的URI
     */
    URI getURI();

    /**
     * 获取该资源的名字
     */
    String getFileName();
}
