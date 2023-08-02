package com.example.y3spring.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class ReflectiveMethodInvocation implements MethodInvocation {
    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Object proceed() throws Throwable {
        return null;
    }

    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return null;
    }
}
