package com.example.y3spring.beans.factory.config;

import com.example.y3spring.beans.factory.ConfigurableListableBeanFactory;
import com.example.y3spring.context.ApplicationContext;
import com.example.y3spring.context.ApplicationContextAware;


import java.util.Properties;

public class PropertyPlaceholderConfigurer extends PropertyResourceConfigurer implements ApplicationContextAware {
    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";

    private ApplicationContext applicationContext;

    // 测试用
    private String location;

    public PropertyPlaceholderConfigurer(){}
    public PropertyPlaceholderConfigurer(ApplicationContext applicationContext){
        setApplicationContext(applicationContext);
    }

    public PropertyPlaceholderConfigurer(ApplicationContext applicationContext,String...locations){
        this(applicationContext);
        setLocations(locations);
    }

    /**
     * 访问BeanFactory中的每一个bean的定义，尝试能否使用props文件中的属性来替换其中的属性占位符
     * @param beanFactory 指定的BeanFactory
     * @param props property属性集合
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) {
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
        for (PropertyValue pv : propertyValues.getPropertyValues()) {
            Object originValue = pv.getValue();
            if(originValue instanceof String){
                String propertyValue = (String) originValue;
                // 如果属性值是占位符 则替换
                if(propertyValue.startsWith(PLACEHOLDER_PREFIX) && propertyValue.endsWith(PLACEHOLDER_SUFFIX)){
                    int length = propertyValue.length();
                    propertyValue = propertyValue.substring(PLACEHOLDER_PREFIX.length(),length-PLACEHOLDER_SUFFIX.length());

                    // 作为key从props中获取属性
                    propertyValue = props.getProperty(propertyValue);
                }
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

    public void setLocation(String location) {
        this.location = location;
        super.setLocations(location);
    }

    public String getLocation() {
        return location;
    }
}
