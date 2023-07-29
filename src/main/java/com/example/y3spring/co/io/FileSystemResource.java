package com.example.y3spring.co.io;

import com.example.y3spring.exception.NestedIOException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 *基于文件系统的资源，对本地文件资源获取
 */
public class FileSystemResource extends AbstractResource{
    private final Path filePath;

    private final File file;

    public FileSystemResource(String filePath) {
        file = new File(filePath);
        this.filePath = Paths.get(file.toURI());
    }

    public FileSystemResource(Path filePath) {
        this.filePath = filePath;
        file = new File(filePath.toUri());
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new FileNotFoundException("文件系统中无法找到该资源:"+filePath);
        }
    }

    @Override
    public URL getURL() {
        try {
            return filePath.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new NestedIOException("不合法的文件路径或URI格式");
        }
    }

    @Override
    public URI getURI(){
        return filePath.toUri();
    }

    @Override
    public String getFileName() {
        return file.getName();
    }
}
