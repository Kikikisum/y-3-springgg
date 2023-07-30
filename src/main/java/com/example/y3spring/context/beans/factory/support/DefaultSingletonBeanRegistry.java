package com.example.y3spring.context.beans.factory.support;

import com.example.y3spring.context.beans.factory.DisposableBean;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean注册表的默认实现类
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<Class<?>, String> beanNamesMap = new ConcurrentHashMap<>();

    private final Map<String, Object> disposableBeans = new ConcurrentHashMap<>();

    @Override
    public void register(String name, Object bean) {
        if (singletonObjects.containsKey(name)) {
            throw new RuntimeException("注册表总存在同名bean，注册失败！！");
        }
        singletonObjects.putIfAbsent(name, bean);
        beanNamesMap.putIfAbsent(bean.getClass(), name);
    }

    @Override
    public Object getSingleBeanByClass(Class<?> beanClass) {
        String name = beanNamesMap.get(beanClass);
        if (name == null) {
            throw new RuntimeException("没有该类型的bean!!");
        }
        Object bean = singletonObjects.get(name);
        if (bean == null) {
            throw new RuntimeException("没有该类型的bean!!");
        }
        return bean;
    }

    @Override
    public Object getSingleBeanByName(String name) {
        return singletonObjects.get(name);
    }


    public void destroySingletons() {
        // 执行所有bean的自定义destroy方法
        Set<String> disposableBeanNames = disposableBeans.keySet();
        for (String disposableBeanName : disposableBeanNames) {
            ((DisposableBean) disposableBeans.get(disposableBeanName)).destroy();
            disposableBeans.remove(disposableBeanName);
        }
        // 清除缓存
        singletonObjects.clear();
        beanNamesMap.clear();
    }

    public void registerDisposableBean(String beanName, Object bean) {
        synchronized (this.disposableBeans) {
            this.disposableBeans.putIfAbsent(beanName, bean);
        }
    }
}
