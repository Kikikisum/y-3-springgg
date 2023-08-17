package com.example.yspring.aop;

import com.example.y3spring.aop.TargetSource;
import com.example.y3spring.aop.aspectj.AspectJExpressionPointcut;
import com.example.y3spring.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.example.y3spring.aop.framework.AdvisedSupport;
import com.example.y3spring.aop.framework.AopProxy;
import com.example.y3spring.aop.framework.AopProxyFactory;
import com.example.y3spring.aop.framework.DefaultAopProxyFactory;
import org.junit.Test;

public class TestDynamicProxy {

    @Test
    public void testJDKProxy(){
        TargetSource targetSource = new TargetSource(new HelloServiceImpl());
        AdvisedSupport advisedSupport = new AdvisedSupport(targetSource);

        //添加前置通知
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor(new TestMethodBeforeAdvice(),
                new AspectJExpressionPointcut("execution(* com.example.y3spring.aop.*.*(..))"));
        advisedSupport.addAdvisor(advisor);
        // 添加后置通知
        AspectJExpressionPointcutAdvisor afterAdvisor = new AspectJExpressionPointcutAdvisor(new TestMethodAfterAdvice(),
                new AspectJExpressionPointcut("execution(* com.example.y3spring.aop.*.*(..))"));
        advisedSupport.addAdvisor(afterAdvisor);
        advisedSupport.setProxyTargetClass(false);

        AopProxyFactory aopProxyFactory = new DefaultAopProxyFactory();
        AopProxy aopProxy = aopProxyFactory.createAopProxy(advisedSupport);

        HelloService helloService = (HelloService) aopProxy.getProxy();
        helloService.hello();
    }

    @Test
    public void testCglibProxy(){
        NiubiService target = new NiubiService();

        TargetSource targetSource = new TargetSource(target);
        AdvisedSupport advisedSupport = new AdvisedSupport(targetSource);
        advisedSupport.setProxyTargetClass(true);

        // 添加前置通知
        AspectJExpressionPointcutAdvisor beforeAdvisor = new AspectJExpressionPointcutAdvisor(new TestMethodBeforeAdvice(),
                new AspectJExpressionPointcut("execution(* com.example.y3spring.aop.*.*(..))"));
        advisedSupport.addAdvisor(beforeAdvisor);
        // 添加后置通知
        AspectJExpressionPointcutAdvisor afterAdvisor = new AspectJExpressionPointcutAdvisor(new TestMethodAfterAdvice(),
                new AspectJExpressionPointcut("execution(* com.example.y3spring.aop.*.*(..))"));
        advisedSupport.addAdvisor(afterAdvisor);

        AopProxyFactory proxyFactory = new DefaultAopProxyFactory();
        AopProxy aopProxy = proxyFactory.createAopProxy(advisedSupport);

        NiubiService niubiService = (NiubiService) aopProxy.getProxy();
        niubiService.niubi();
        niubiService.six("6666");
    }

}
