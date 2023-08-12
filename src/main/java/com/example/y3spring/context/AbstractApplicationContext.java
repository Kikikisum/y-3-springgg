package com.example.y3spring.context;

import com.example.y3spring.aop.autoproxy.AbstractAdvisorAutoProxyCreator;
import com.example.y3spring.beans.factory.co.io.DefaultResourceLoader;
import com.example.y3spring.beans.factory.ConfigurableListableBeanFactory;
import com.example.y3spring.beans.factory.config.BeanFactoryPostProcessor;
import com.example.y3spring.beans.factory.config.BeanPostProcessor;
import com.example.y3spring.beans.factory.config.PropertyPlaceholderConfigurer;
import com.example.y3spring.beans.factory.support.ApplicationContextAwareProcessor;
import com.example.y3spring.beans.factory.support.ApplicationListenerDetector;
import com.example.y3spring.beans.factory.exception.BeansException;
import com.example.y3spring.context.event.ContextClosedEvent;
import com.example.y3spring.context.event.ContextRefreshedEvent;
import com.example.y3spring.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.Executor;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext{

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    // 事件传播器
    private ApplicationEventMulticaster applicationEventMulticaster;

    // 异步线程池 由用户决定是否添加
    private Executor taskExecutor;

    //todo 模板方法 只提供了抽象逻辑 具体逻辑由子类实现 -- 有待完善
    @Override
    public void refresh() {

        // 获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = obtainBeanFactory();

        // 为新的内置BeanFactory填充特殊内置属性
        prepareBeanFactory(beanFactory);

        // 在实例化bean之前调用beanFactoryPostProcessor 看是否需要修改beanDefinition
        //todo 问题：BeanFactoryPostProcessor在哪里开始注册进IOC容器
        //          在prepareBeanFactory中 注册了BeanPostProcessor和BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 注册BeanPostProcessors进当前内置的BeanFactory
        registerBeanPostProcessors(beanFactory);

        //---- 注册其他上下文应提供的拓展组件 ---

        //注册事件广播器
        initApplicationEventMulticaster();

        // ----------------------

        // 预实例化所有的bean
        beanFactory.preInitiateSingletons();

        // 上下文刷新完毕 发布事件
        publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * 为指定的BeanFactory填充特殊属性，一般用于为BeanFactory初始化后置处理器
     */
    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        // 添加处理上下文感知的后置处理器
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
        // 添加AOP自动创建代理对象用的实例化处理器
        beanFactory.addBeanPostProcessor(new AbstractAdvisorAutoProxyCreator(beanFactory));
        beanFactory.addBeanFactoryPostProcessor(new PropertyPlaceholderConfigurer(this));
    }

    /**
     * 启动并获取BeanFactory，若已启动则重启BeanFactory
     */
    protected ConfigurableListableBeanFactory obtainBeanFactory(){
        // 启动上下文 创建内置的beanFactory 从xml文件中加载所有的beanDefinition（包括特殊bean）
        refreshBeanFactory();
        // 获取BeanFactory
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
    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 执行指定BeanFactory的所有后置处理器
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory){
        // 先从bean定义中获取
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true);
        for (String postProcessorName : postProcessorNames) {
            Object bean = beanFactory.getBean(postProcessorName);
            if(bean instanceof BeanFactoryPostProcessor){
                ((BeanFactoryPostProcessor) bean).postProcessBeanFactory(beanFactory);
            }else {
                throw new BeansException("the bean name with: ["+postProcessorName+"] is not a BeanFactoryPostProcessor");
            }
        }
        // 再从预注册的BeanFactoryProcessor中获取
        for (BeanFactoryPostProcessor beanFactoryBeanPostProcessor : beanFactory.getBeanFactoryBeanPostProcessors()) {
            beanFactoryBeanPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 为指定的beanFactory注册所有BeanPostProcessor（实际上应该按照优先级先后注册）
     * @param beanFactory 内置的BeanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory){

        // 注册自定义的BeanPostProcessor
        //todo 真实的BeanPostProcessor应该有执行优先级 所以要按优先级排序后再放入
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true);
        for (String postProcessorName : postProcessorNames) {
            // 由指定BeanFactory管理该BeanPostProcessor bean
            Object bean = beanFactory.getBean(postProcessorName);
            if(bean instanceof BeanPostProcessor){
                beanFactory.addBeanPostProcessor((BeanPostProcessor) bean);
            }else {
                throw new BeansException("the bean name with: ["+postProcessorName+"] is not a BeanPostProcessor");
            }
            if(bean instanceof ApplicationContextAware){
                ((ApplicationContextAware) bean).setApplicationContext(this);
            }
        }
    }

    /**
     * 初始化上下文的事件传播器
     */
    protected void initApplicationEventMulticaster(){

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 一个BeanFactory只管理一个传播器实例（特殊bean） 所以直接获取
        ApplicationEventMulticaster applicationEventMulticaster = (ApplicationEventMulticaster) getBeanFactory().getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);

        if (applicationEventMulticaster == null) {
            applicationEventMulticaster = new SimpleApplicationEventMulticaster();

            // 判断有无异步线程池 有则注入
            if(this.taskExecutor != null){
                applicationEventMulticaster.addTaskExecutor(taskExecutor);
            }
            // 手动注册进容器中
            beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
        }

        this.applicationEventMulticaster = applicationEventMulticaster;

    }

    /**
     * 模板方法 实际逻辑交给子类实现
     */
    @Override
    public void close() {

        // 先销毁所有的bean实例
        destroyBeans();
        // 再关闭内置的BeanFactory
        closeBeanFactory();

        // 上下文已关闭 发布事件
        publishEvent(new ContextClosedEvent(this));
    }

    /**
     * 销毁所有的bean实例
     */
    protected void destroyBeans(){
        getBeanFactory().destroySingletons();
    }

    /**
     * 关闭内置的BeanFactory
     */
    protected abstract void closeBeanFactory();

    /**
     * 委托给内置BeanFactory实现
     * @param beanName bean的名字
     */
    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    /**
     * 委托给内置BeanFactory实现
     * @param beanName bean的名字
     * @param requiredType 需要的类型
     */
    @Override
    public Object getBean(String beanName, Class<?> requiredType) {
        return getBeanFactory().getBean(beanName,requiredType);
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


    public void publishEvent(ApplicationEvent event) {
        if(event == null){
            throw new IllegalArgumentException("The event passed in cannot be empty");
        }
        getApplicationEventMulticaster().multicastEvent(event);
    }

    protected ApplicationEventMulticaster getApplicationEventMulticaster() {
        return applicationEventMulticaster;
    }

    protected void setApplicationEventMulticaster(ApplicationEventMulticaster applicationEventMulticaster) {
        this.applicationEventMulticaster = applicationEventMulticaster;
    }

    /**
     * 实现自{@link ConfigurableApplicationContext}，向当前上下文的广播器注册指定的监听器
     * @param listener 要注册的监听器对象
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationEventMulticaster.addApplicationListener(listener);
    }

    @Override
    public void addTaskExecutor(Executor executor) {
        if(executor != null){
            this.taskExecutor = executor;
            applicationEventMulticaster.addTaskExecutor(executor);
        }
    }


}
