package com.example.y3spring.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import com.example.y3spring.aop.ClassFilter;
import com.example.y3spring.aop.MethodMatcher;
import com.example.y3spring.aop.support.ExpressionPointcut;


import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AspectJExpressionPointcut implements ExpressionPointcut, ClassFilter, MethodMatcher {
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    private PointcutExpression pointcutExpression;

    private PointcutParser pointcutParser;

    private String expression;

    public AspectJExpressionPointcut(){
        pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
    }

    public AspectJExpressionPointcut(String expression) {
        this();
        this.expression=expression;
        pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public String getExpression() {
        return pointcutExpression.getPointcutExpression();
    }

    public void setExpression(String expression){
        this.expression=expression;
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }
}
