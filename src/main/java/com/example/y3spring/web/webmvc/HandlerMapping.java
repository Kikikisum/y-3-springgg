package com.example.y3spring.web.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class HandlerMapping {
    // 处理请求的具体Controller对象
    private Object Controller;
    // 处理请求的具体方法
    private Method method;

    // 处理的请求路径
    private Pattern pattern;

    public HandlerMapping(Object controller, Method method, Pattern pattern) {
        Controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return Controller;
    }

    public void setController(Object controller) {
        Controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
