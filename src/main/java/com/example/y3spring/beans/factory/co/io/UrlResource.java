package com.example.y3spring.beans.factory.co.io;

import com.example.y3spring.beans.factory.exception.NestedIOException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 基于url获取资源
 */
public class UrlResource extends AbstractResource {
    private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new NestedIOException("不存在该url路径对应的资源");
        }
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public String getFileName() {
        return url.getFile();
    }
}
