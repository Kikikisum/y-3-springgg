package com.example.y3spring.beans.factory.io;

import com.example.y3spring.beans.factory.exception.NestedIOException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class AbstractResource implements Resource {
    @Override

    public boolean isExist()
    {
        try {
            // 调用具体实现类的getInputStream获取二进制流 若能获取则存在
            InputStream inputStream = getInputStream();
            inputStream.close();

            return true;

        } catch (IOException e) {
            return false;
        }
    }
    @Override
    public URI getURI()
    {
        try {
            URL url = getURL();
            return url.toURI();
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new NestedIOException("不合法的URL格式",e);
        }
    }

}
