package com.example.y3spring.aop.framework;

import com.example.y3spring.aop.Advisor;
import com.example.y3spring.aop.MethodMatcher;
import com.example.y3spring.aop.PointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DefaultAdvisorChainFactory implements AdvisorChainFactory{
    @Override
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass) {

        Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());

        List<Object> interceptorList = new ArrayList<>();

        List<Advisor> advisors = config.getAdvisors();
        for (Advisor advisor : advisors) {
            if(advisor instanceof PointcutAdvisor){
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                // 判断类是否符合切入点的表达式 符合就继续判断
                if(pointcutAdvisor.getPointcut().getClassFilter().matches(targetClass)){
                    MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                    boolean match = mm.matches(method, targetClass);
                    // 如果方法也匹配 则说明当前Advisor可以被执行 获取拦截它的所有拦截器
                    if(match){

                    }
                }

            }else {


            }
        }

        return null;
    }
}
