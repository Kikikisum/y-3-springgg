package com.example.y3spring.context;

import com.example.y3spring.co.io.ResourceLoader;
import com.example.y3spring.context.beans.factory.HierarchicalBeanFactory;
import com.example.y3spring.context.beans.factory.ListableBeanFactory;

/**
 * 上下文的中央接口，可获取上下文信息
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader {
}
