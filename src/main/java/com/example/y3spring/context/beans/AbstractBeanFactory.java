package com.example.y3spring.context.beans;



public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String beanName) {

        Object bean = getSingleBeanByName(beanName);
        // 如果bean为空，则说明注册表中没有 需要在工厂创建
        if(bean == null){
            // 双重校验
            synchronized (AbstractBeanFactory.class){
                bean = getSingleBeanByName(beanName);
                if(bean == null){
                    bean = createBean(beanName,getBeanDefinition(beanName));
                    // 注册进注册表
                    register(beanName,bean);
                }
            }
        }
        return bean;
    }

    /**
     * 创建名为beanName的Bean实例
     * 创建策略由实现类决定
     */
    protected abstract <T> T createBean(String beanName, BeanDefinition<T> beanDefinition);

    /**
     * 根据beanName获取它的bean定义
     */
    protected abstract BeanDefinition<?> getBeanDefinition(String beanName);
}
