package com.example.y3spring.context.scanner;

import cn.hutool.core.util.StrUtil;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.context.annotation.Component;
import com.example.y3spring.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {
    public Set<BeanDefinition<?>> doScan(String[] basePackages){

        Set<BeanDefinition<?>> beanDefinitionSet = new HashSet<>();
        for (String basePackage : basePackages) {
            for (BeanDefinition<?> candidate : scanCandidateComponents(basePackage)) {
                String scope = resolveBeanScope(candidate);
                if(StrUtil.isNotBlank(scope)){
                    candidate.setScope(scope);
                }
                beanDefinitionSet.add(candidate);
            }
        }

        return beanDefinitionSet;
    }

    public String resolveBeanScope(BeanDefinition<?> beanDefinition){
        Class<?> clazz = beanDefinition.getType();
        Scope scope = clazz.getAnnotation(Scope.class);
        if(scope != null){
            return scope.value();
        }
        // 默认单例
        return "singleton";
    }

    public String determineBeanName(BeanDefinition<?> beanDefinition){
        Class<?> clazz = beanDefinition.getType();
        Component annotation = clazz.getAnnotation(Component.class);
        String value = annotation.value();
        if(StrUtil.isNotBlank(value)){
            return value;
        }
        // 默认beanName为 类名首字母小写
        return StrUtil.lowerFirst(clazz.getSimpleName());
    }
}
