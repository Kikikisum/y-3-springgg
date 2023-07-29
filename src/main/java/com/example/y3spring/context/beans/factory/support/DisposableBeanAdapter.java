package com.example.y3spring.context.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.example.y3spring.context.beans.factory.DisposableBean;
import com.example.y3spring.context.beans.factory.config.BeanDefinition;
import com.example.y3spring.exception.BeansException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 在给定的bean实例上执行各种销毁步骤的适配器
 */
public class DisposableBeanAdapter implements DisposableBean,Runnable {
    private static final String DESTROY_METHOD_NAME = "destroy";

    private static final String CLOSEABLE_METHOD_NAME = "close";

    private String beanName;

    private Object bean;

    private BeanDefinition beanDefinition;

    private String destroyMethodName;

    // 是否实现了DisposableBean接口
    private boolean invokeDisposableBean;

    // 是否实现了AutoCloseable接口 并且关闭方法名为destroy
    private boolean isAutoCloseable;

    private Method destroyMethod;

    public DisposableBeanAdapter(String beanName, Object bean, BeanDefinition beanDefinition){

        this.beanName = beanName;
        this.bean = bean;
        this.beanDefinition = beanDefinition;

        // 实现了DisposableBean接口 并且没有外部管理的destroy方法和destroy同名 --> 避免执行同一方法两次
        invokeDisposableBean = (bean instanceof DisposableBean) && !DESTROY_METHOD_NAME.equals(beanDefinition.getDestroyMethodName());

        // 查看bean定义中是否指定了destroy-method
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if(StrUtil.isNotBlank(destroyMethodName)){
            this.destroyMethodName = destroyMethodName;
        }
    }
    /**
     * 作为一个DisposableBean接口执行
     */
    @Override
    public void destroy() {
        if(invokeDisposableBean){
            ((DisposableBean) bean).destroy();
        }

        if(StrUtil.isNotBlank(destroyMethodName)){
            Method destroyMethod = ClassUtil.getPublicMethod(bean.getClass(), destroyMethodName);
            invokeCustomDestroyMethod(destroyMethod);

        }else if(this.destroyMethod != null){
            invokeCustomDestroyMethod(this.destroyMethod);
        }
    }

    /**
     * 作为一个异步线程任务执行
     */
    @Override
    public void run() {
        destroy();
    }


    /**
     * 执行指定的destroyMethod，封装了处理异常的逻辑
     * @param destroyMethod 自定义的destroy方法
     */
    public void invokeCustomDestroyMethod(Method destroyMethod){
        int parameterCount = destroyMethod.getParameterCount();
        if(parameterCount > 0){
            throw new BeansException("The specified destroy method ["  + destroyMethod.getName() + "] has more than zero parameter");
        }
        Object[] args = new Object[0];
        try {
            destroyMethod.invoke(bean,args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
