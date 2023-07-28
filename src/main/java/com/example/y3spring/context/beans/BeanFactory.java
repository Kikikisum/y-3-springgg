package com.example.y3spring.context.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单的bean容器类BeanFactory
 */
public class BeanFactory {
    private Map<String,Object> beanMap=new HashMap<>();

    /**
     * 注册bean
     * @param name bean的名字
     * @param bean bean
     */
    public void register(String name,Object bean) {
        beanMap.put(name,bean);
    }

    /**
     * 通过bean的名字获取bean
     * @param name
     * @return
     */
    public Object getBean(String name){
        return beanMap.get(name);
    }

}
