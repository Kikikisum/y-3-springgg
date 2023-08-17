package com.example.yspringtest.aop;

import com.example.y3spring.aop.aspectj.AspectJExpressionPointcut;
import org.junit.Test;

import java.lang.reflect.Method;

public class testPointCut {

    @Test
    public void testPointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* sillybaka.springframework.aop.*.*(..))");

        Class<TestService> clazz = TestService.class;

        // 查看匹配类
        System.out.println(pointcut.matches(clazz));

        // 匹配方法
        Method testPointCut = clazz.getDeclaredMethod("testPointCut");

        System.out.println(pointcut.matches(testPointCut, clazz));

        System.out.println(pointcut.matches(com.example.yspringtest.aop.testPointCut.class));

        Method test2 = clazz.getDeclaredMethod("test2", Integer.TYPE);
        System.out.println(pointcut.matches(test2,clazz));
    }
}
