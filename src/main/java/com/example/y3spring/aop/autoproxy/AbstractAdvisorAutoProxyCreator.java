package com.example.y3spring.aop.autoproxy;

import com.example.y3spring.aop.Advisor;
import com.example.y3spring.aop.ClassFilter;
import com.example.y3spring.aop.Pointcut;
import com.example.y3spring.aop.TargetSource;
import com.example.y3spring.aop.adapter.AdvisorAdapterRegistry;
import com.example.y3spring.aop.adapter.GlobalAdvisorAdapterRegistry;
import com.example.y3spring.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.example.y3spring.aop.framework.AdvisedSupport;
import com.example.y3spring.aop.framework.AopProxyFactory;
import com.example.y3spring.aop.framework.DefaultAopProxyFactory;
import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.beans.factory.BeanFactoryAware;
import com.example.y3spring.beans.factory.ConfigurableListableBeanFactory;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.example.y3spring.beans.factory.support.DefaultListableBeanFactory;
import com.example.y3spring.beans.factory.support.SimpleInstantiationStrategy;
import org.aopalliance.aop.Advice;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于自动将Bean按照已注册的aop配置，封装成一个代理对象
 */

public class AbstractAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private final AdvisorAdapterRegistry advisorAdapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();

    private DefaultListableBeanFactory beanFactory;

    public AbstractAdvisorAutoProxyCreator(){}

    public AbstractAdvisorAutoProxyCreator(ConfigurableListableBeanFactory beanFactory){
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {

        // 检查当前bean类型是否为AOP基础构件
        if(isInfrastructureClass(beanClass)){
            // 如果是则无法生成代理对象
            return null;
        }

        BeanDefinition<?> beanDefinition = beanFactory.getBeanDefinition(beanName, beanClass);
        // 正常这里应该是从IOC容器的某注册表中获取该bean定义对应的快照实例
        TargetSource targetSource = new TargetSource(new SimpleInstantiationStrategy().instantiation(beanDefinition));

        return createProxy(beanClass,beanName,targetSource);
    }


    /**
     * 为指定bean创建代理对象的实际逻辑
     */
    protected Object createProxy(Class<?> beanClass,String beanName,TargetSource targetSource){
        // 创建IOC配置类对象
        AdvisedSupport advisedSupport = new AdvisedSupport();

        advisedSupport.setTargetSource(targetSource);
        // 默认采用Cglib动态代理
        advisedSupport.setProxyTargetClass(true);

        // todo 这里先获取大家都通用的Advisor，后面应该可能有指定给哪个Bean的Advisor
        String[] advisorNames = beanFactory.getBeanNamesForType(Advisor.class, true);

        List<Object> advisors = new ArrayList<>();
        for (String advisorName : advisorNames) {
            Object advisorObject = beanFactory.getBean(advisorName);
            // 匹配每一个Advisor
            if(advisorObject instanceof AspectJExpressionPointcutAdvisor){
                AspectJExpressionPointcutAdvisor advisor = (AspectJExpressionPointcutAdvisor) advisorObject;
                Pointcut pointcut = advisor.getPointcut();
                ClassFilter classFilter = pointcut.getClassFilter();
                // 判断该Advisor是否匹配当前要代理的bean
                if(classFilter.matches(beanClass)){
                    advisors.add(advisor);
                }
            }
        }
        // 如果没有对应的Advisor 则说明无法生成代理对象 返回null
        if(advisors.size() == 0){
            return null;
        }
        advisedSupport.addAdvisors(advisors.toArray(new Advisor[0]));

        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();

        return aopProxyFactory.createAopProxy(advisedSupport).getProxy();
    }

    /**
     * 检查将要被代理的类是否是AOP的基础构件
     * @param targetClass 被代理类
     */
    protected boolean isInfrastructureClass(Class<?> targetClass){
        boolean retVal = Advice.class.isAssignableFrom(targetClass) ||
                Advisor.class.isAssignableFrom(targetClass) ||
                Pointcut.class.isAssignableFrom(targetClass);

        return retVal;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
