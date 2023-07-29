package com.example.y3spring.context.beans.factory;

import com.example.y3spring.context.beans.factory.config.BeanReference;
import com.example.y3spring.context.beans.factory.config.PropertyValue;
import com.example.y3spring.context.beans.factory.config.PropertyValues;
import com.example.y3spring.context.beans.factory.support.AbstractBeanFactory;
import com.example.y3spring.context.beans.factory.config.InstantiationStrategy;
import com.example.y3spring.context.beans.factory.support.SimpleInstantiationStrategy;
import com.example.y3spring.context.beans.factory.utils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory{
    private static final InstantiationStrategy INSTANTIATION_STRATEGY = new SimpleInstantiationStrategy();

    @Override
    protected <T> T createBean(BeanDefinition<T> beanDefinition) {
        return doCreateBean(beanDefinition);
    }

    /**
     * 创建Bean实例的实际逻辑
     */
    public <T> T doCreateBean(BeanDefinition<T> beanDefinition){

        T beanInstance = INSTANTIATION_STRATEGY.instantiation(beanDefinition);
        autoWirePropertyValues(beanInstance,beanDefinition);
        return beanInstance;
    }

    /**
     * 为bean实例对象自动装配属性 (底层使用setter注入）
     * @param bean 实例对象
     * @param beanDefinition bean定义
     */
    public <T> void autoWirePropertyValues(T bean, BeanDefinition<T> beanDefinition){
        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        Class<?> clazz = bean.getClass();

        // 获取该Bean类的所有PropertyDescriptor
        //todo 获取该Bean类的所有PropertyDescriptor --> 生命周期开始为在XMl扫描时按需调用
        Map<String, PropertyDescriptor> beanPropertyMap = PropertyUtils.getBeanPropertyMap(clazz);
        Map<String, Class<?>> propertyTypeMap = PropertyUtils.getPropertyTypeMap(clazz);
        for(PropertyValue pv : propertyValues.getPropertyValues()){

            String propertyName = pv.getName();
            Object propertyValue = pv.getValue();

            try {
                //todo xml配置中 普通属性不能配置类型 应该以其他方式获取
                PropertyDescriptor pd = beanPropertyMap.get(propertyName);
                // 从属性类型map中获取属性类型
                Class<?> propertyType = propertyTypeMap.get(propertyName);

                Method setterMethod = pd.getWriteMethod();
                setterMethod.invoke(bean, propertyValue);
                // 引用类型
                if(propertyType == BeanReference.class){

                    BeanReference beanReference = (BeanReference) propertyValue;
                    String beanName = beanReference.getName();
                    //todo 获取Bean实例 --> 1、有可能该Bean实例尚未创建 应该重写为阻塞等待
                    //                    2、有可能会发生循环依赖，在后面再解决
                    Object innerBean = getBean(beanName);
                    setterMethod.invoke(bean,innerBean);

                }else if(propertyType == BeanDefinition.class){
                    // Bean类型（级联定义了一个Bean）
                    //todo 先根据Bean定义创建Bean实例  --> 有可能破坏单例 需要保存在多例注册表中
                    BeanDefinition<?> innerBeanDefinition = (BeanDefinition<?>) propertyValue;
                    Object innerBean = createBean(innerBeanDefinition);
                    setterMethod.invoke(bean,innerBean);
                }else {
                    // 普通属性 直接使用Setter方法注入
                    setterMethod.invoke(bean, propertyValue);
                }

            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
