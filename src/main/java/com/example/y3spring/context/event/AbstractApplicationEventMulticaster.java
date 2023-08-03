package com.example.y3spring.context.event;

import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.beans.factory.BeanFactoryAware;
import com.example.y3spring.beans.factory.ConfigurableBeanFactory;
import com.example.y3spring.beans.factory.exception.BeansException;
import com.example.y3spring.context.ApplicationEvent;
import com.example.y3spring.context.ApplicationEventMulticaster;
import com.example.y3spring.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {
    /**
     * 用于保存绑定的监听者，在同一上下文中无法注册完全相同的监听者
     */
    protected final Set<ApplicationListener<?>> applicationListeners = new HashSet<>(16);

    private ConfigurableBeanFactory beanFactory;

    @Override
    public void addApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.add(applicationListener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> applicationListener) {
        applicationListeners.remove(applicationListener);
    }

    @Override
    public void addApplicationListenerBean(String beanName) {
        if(beanFactory == null){
            throw new BeansException("the inner beanFactory is null");
        }
        Object bean = beanFactory.getBean(beanName);
        // 如果不是监听器bean
        if(!(bean instanceof ApplicationListener)){
            throw new BeansException("the bean named ["+ beanName +"] is not a ApplicationListener");
        }
        applicationListeners.add((ApplicationListener<?>) bean);
    }

    @Override
    public void removeApplicationListenerBean(String beanName) {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableBeanFactory) beanFactory;
    }

    /**
     * 检查目标监听器是否对指定事件感兴趣
     * @param listener 要检查的监听器
     * @param event 指定事件
     */
    //todo 这里的判断逻辑可以优化 可以先将event解析成type 再使用type去检查
    protected boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event){
        // 检查当前listener监听的事件类型（检查泛型的类型）
        Type[] genericInterfaces = listener.getClass().getGenericInterfaces();

        Class<? extends ApplicationEvent> eventType = event.getClass();

        for (Type type : genericInterfaces) {
            // 如果是参数型类型
            if(type instanceof ParameterizedType){
                // 获得实际代表的类型列表
                Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
                for (Type actualType : actualTypes) {
                    String actualTypeName = actualType.getTypeName();
                    try {
                        if(Class.forName(actualTypeName).isAssignableFrom(eventType)){
                            return true;
                        }
                    } catch (ClassNotFoundException e) {
                        throw new BeansException("wrong event type name:"+actualTypeName,e);
                    }
                }
            }
        }
        return false;
    }
}
