package com.example.y3spring.beans.factory.config;

import com.example.y3spring.beans.factory.ConfigurableListableBeanFactory;
import com.example.y3spring.beans.factory.utils.PlaceholderResolvingStringValueResolver;
import com.example.y3spring.context.ApplicationContext;
import com.example.y3spring.context.ApplicationContextAware;

import java.util.Properties;

public class PlaceholderConfigurerSupport  extends PropertyResourceConfigurer implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private PlaceholderResolvingStringValueResolver placeholderResolver;

    public PlaceholderConfigurerSupport(){}

    public PlaceholderConfigurerSupport(ApplicationContext applicationContext){
        setApplicationContext(applicationContext);
    }

    /**
     * 访问BeanFactory中的每一个bean的定义，尝试能否使用props文件中的属性来替换其中的属性占位符
     * @param beanFactory 指定的BeanFactory
     * @param props property属性集合
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {

        // 创建新的占位符解析器 并注入容器
        placeholderResolver = new PlaceholderResolvingStringValueResolver(props);
        beanFactory.addEmbeddedValueResolver(placeholderResolver);

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition<?> beanDefinition = beanFactory.getBeanDefinitionByName(beanDefinitionName);
            resolvePropertyValues(beanDefinition,props);
        }
    }

    /**
     * 根据props解析并替换目标bean定义中的占位符<p>
     * 占位符格式： ${propertyName}
     * @param beanDefinition 目标bean定义
     * @param props property属性集合
     */
    protected void resolvePropertyValues(BeanDefinition<?> beanDefinition,Properties props){
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if(propertyValues == null){
            return;
        }
        for (PropertyValue pv : propertyValues.getPropertyValues()) {
            Object originValue = pv.getValue();
            if(originValue instanceof String){
                String propertyValue = (String) originValue;
                // 解析占位符并替换为属性
                propertyValue = this.placeholderResolver.resolveStringValue(propertyValue);

                propertyValues.addPropertyValue(new PropertyValue(pv.getName(),propertyValue));
            }
        }
        beanDefinition.setPropertyValues(propertyValues);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        setResourceLoader(applicationContext);
    }

}
