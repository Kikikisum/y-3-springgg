package com.example.y3spring.context.scanner;

import cn.hutool.core.util.ClassUtil;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.context.annotation.Component;

import java.util.HashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {
    /**
     * 从指定的包读取所有候选components的bean定义
     * @param basePackage 指定的包
     * @return 指定包下所有Component的bean定义
     */
    protected Set<BeanDefinition<?>> scanCandidateComponents(String basePackage){
        Set<Class<?>> classSet = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        Set<BeanDefinition<?>> beanDefinitionSet = new HashSet<>();
        for (Class<?> clazz : classSet) {
            beanDefinitionSet.add(parseClassToBeanDefinition(clazz));
        }
        return beanDefinitionSet;
    }

    /**
     * 将指定类转换成相应的BeanDefinition
     * @param clazz 指定类
     */
    protected BeanDefinition<?> parseClassToBeanDefinition(Class clazz){
        BeanDefinition<?> beanDefinition = new BeanDefinition<>();
        beanDefinition.setType(clazz);

        return beanDefinition;
    }
}
