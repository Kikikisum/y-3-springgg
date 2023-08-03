package com.example.y3spring.aop.framework;

import com.example.y3spring.aop.Advisor;
import com.example.y3spring.aop.TargetSource;
import com.example.y3spring.aop.support.DefaultPointcutAdvisor;
import org.aopalliance.aop.Advice;

import java.util.ArrayList;
import java.util.List;

public class AdvisedSupport {
    /**
     * 被Aop的目标对象（连接点）
     */
    private TargetSource targetSource;
    /**
     * 连接点对应的通知链
     */
    private final List<Advisor> advisors = new ArrayList<>();
    /**
     * 连接点实现的接口（用于创建Jdk动态代理对象）
     */
    private final List<Class<?>> interfaces = new ArrayList<>();
    /**
     * 是否代理目标类
     * 若是 则为Cglib动态代理
     * 若否 则为Jdk动态代理
     */
    private boolean isProxyTargetClass=true;

    private Class<?>[] cacheInterfaces;
    /**
     * 通知链工厂
     */
    private final AdvisorChainFactory chainFactory = new DefaultAdvisorChainFactory();

    public AdvisedSupport(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public AdvisedSupport(){}

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public void addAdvisor(Advisor advisor){
        if(advisor != null){
            this.advisors.add(advisor);
            this.cacheInterfaces = null;
        }
    }
    public void addAdvice(Advice advice){
        addAdvisor(advice);
    }

    public void addAdvisor(Advice advice){
        if(advice != null){
            this.advisors.add(new DefaultPointcutAdvisor(advice));
            this.cacheInterfaces = null;
        }
    }
    public void addAdvisors(Advisor ... advisors){
        for (Advisor advisor : advisors){
            if(advisor != null){
                this.advisors.add(advisor);
                this.cacheInterfaces = null;
            }
        }
    }

    public void addInterface(Class<?> inf){
        if(inf != null){
            interfaces.add(inf);
            this.cacheInterfaces = null;
        }
    }
    public void addInterfaces(Class<?> ... interfaces){
        for (Class<?> inf : interfaces) {
            if(inf != null){
                this.interfaces.add(inf);
                this.cacheInterfaces = null;
            }
        }
    }

    public List<Advisor> getAdvisors() {
        return this.advisors;
    }

    public AdvisorChainFactory getAdvisorChainFactory(){
        return this.chainFactory;
    }

    public Class<?> getTargetClass(){
        return targetSource.getTargetClass();
    }

    public Class<?>[] getInterfaces(){
        return targetSource.getTargetClass().getInterfaces();
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        isProxyTargetClass = proxyTargetClass;
    }

    public boolean isProxyTargetClass() {
        return isProxyTargetClass;
    }
}
