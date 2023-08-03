package com.example.y3spring.aop.adapter;

import com.example.y3spring.aop.Advisor;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存Spring支持的通知、拦截器的适配器
 */
public class DefaultAdvisorAdapterRegistry implements AdvisorAdapterRegistry{
    private final List<AdvisorAdapter> adapters = new ArrayList<>();

    public DefaultAdvisorAdapterRegistry(){
        // 注册内置的adapter
        this.adapters.add(new MethodBeforeAdviceAdapter());
        this.adapters.add(new MethodAfterAdviceAdapter());
    }

    @Override
    public void registerAdvisorAdapter(AdvisorAdapter adapter) {
        this.adapters.add(adapter);
    }

    @Override
    public MethodInterceptor[] getInterceptors(Advisor advisor) {
        List<MethodInterceptor> interceptors = new ArrayList<>();
        Advice advice = advisor.getAdvice();

        // 在注册表中查看是否有支持该类型通知的适配器
        for(AdvisorAdapter adapter : adapters){
            if(adapter.supportAdvice(advice)){
                interceptors.add(adapter.getInterceptor(advisor));
            }
        }

        return interceptors.toArray(new MethodInterceptor[0]);
    }
}
