package com.example.y3spring.context;

import com.example.y3spring.beans.factory.co.io.ResourceLoader;
import com.example.y3spring.beans.factory.HierarchicalBeanFactory;
import com.example.y3spring.beans.factory.config.ListableBeanFactory;
import com.example.y3spring.beans.factory.support.BeanDefinitionReader;

import java.util.Properties;

/**
 * 上下文的中央接口，可获取上下文信息
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {
    
    Properties getConfig();
}
