package com.example.y3spring.context;

import com.example.y3spring.beans.factory.io.DefaultResourceLoader;
import com.example.y3spring.beans.factory.ConfigurableListableBeanFactory;
import com.example.y3spring.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.beans.factory.support.DefaultListableBeanFactory;
import com.example.y3spring.beans.factory.exception.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext {
    /**
     * 启动或刷新上下文
     * 在实例化ApplicationContext的时候按需调用
     */
    void refresh();

    /**
     * 关闭上下文
     */
    void close();

    abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

        @Override
        public void refresh() {
            // 启动上下文 创建内置的beanFactory 从xml文件中加载所有的beanDefinition
            refreshBeanFactory();

            DefaultListableBeanFactory beanFactory = getBeanFactory();
            invokeBeanFactoryPostProcessors(beanFactory);
            registerBeanPostProcessors(beanFactory);

            // 预实例化所有的bean
            beanFactory.preInitiateSingletons();

        }


        @Override
        public void close() {

        }

        public ConfigurableListableBeanFactory obtainBeanFactory(){
            refreshBeanFactory();
            return getBeanFactory();
        }

        /**
         * 启动上下文内置的BeanFactory
         * 由子类 AbstractRefreshableApplicationContext 实现
         */
        protected abstract void refreshBeanFactory();

        /**
         * 获取当前上下文的内置BeanFactory
         * 由子类 AbstractRefreshableApplicationContext 实现
         */
        protected abstract DefaultListableBeanFactory getBeanFactory();

        /**
         * 执行指定BeanFactory的所有后置处理器
         */
        public void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory){
            String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true);
            for (String postProcessorName : postProcessorNames) {
                Object bean = beanFactory.getBean(postProcessorName);
                if(bean instanceof BeanFactoryPostProcessor){
                    ((BeanFactoryPostProcessor) bean).postProcessBeanFactory(beanFactory);
                }else {
                    throw new BeansException("the bean name with: ["+postProcessorName+"] is not a BeanFactoryPostProcessor");
                }
            }
        }

        /**
         * 为指定beanFactory注册所有BeanPostProcessor
         * @param beanFactory
         */
        public void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory){
            String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true);
            for (String postProcessorName : postProcessorNames) {
                // 由指定BeanFactory管理该BeanPostProcessor bean
                Object bean = beanFactory.getBean(postProcessorName);
                if(bean instanceof BeanPostProcessor){
                    beanFactory.addBeanPostProcessor((BeanPostProcessor) bean);
                }else {
                    throw new BeansException("the bean name with: ["+postProcessorName+"] is not a BeanPostProcessor");
                }
            }
        }


        /**
         * 委托给内置BeanFactory处理
         * @param beanName bean的名字
         */
        @Override
        public Object getBean(String beanName) {
            return getBeanFactory().getBean(beanName);
        }

        /**
         * 委托给内置BeanFactory处理
         * @param type bean类型
         * @param includeNonSingleton 是否包含非单例的bean
         */
        @Override
        public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingleton) {
            return getBeanFactory().getBeanNamesForType(type,includeNonSingleton);
        }
    }
}
