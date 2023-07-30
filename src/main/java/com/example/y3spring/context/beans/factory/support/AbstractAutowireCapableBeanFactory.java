package com.example.y3spring.context.beans.factory.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.example.y3spring.context.beans.factory.AutowireCapableBeanFactory;
import com.example.y3spring.context.beans.factory.DisposableBean;
import com.example.y3spring.context.beans.factory.InitializingBean;
import com.example.y3spring.context.beans.factory.config.*;
import com.example.y3spring.context.beans.factory.utils.PropertyUtils;
import com.example.y3spring.exception.BeansException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private static final InstantiationStrategy INSTANTIATION_STRATEGY = new SimpleInstantiationStrategy();

    @Override
    protected <T> T createBean(String name,BeanDefinition<T> beanDefinition) {
        return doCreateBean(name,beanDefinition);
    }

    /**
     * 创建Bean实例的实际逻辑
     */
    public <T> T doCreateBean(String name,BeanDefinition<T> beanDefinition){

        T beanInstance = INSTANTIATION_STRATEGY.instantiation(beanDefinition);
        autoWirePropertyValues(name,beanInstance,beanDefinition);

        try {
            beanInstance = initializeBean(name,beanInstance,beanDefinition);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new BeansException("bean initialize error",e);
        }

        // 检查当前bean是否有自定义的destroy方法，若有则需要注册进注册表
        registerDisposableBeanIfNecessary(name,beanInstance,beanDefinition);
        return beanInstance;
    }

    /**
     * 为bean实例对象自动装配属性 (底层使用setter注入）
     * @param existingBean 实例对象
     * @param beanDefinition bean定义
     */
    @Override
    public <T> void autoWirePropertyValues(String beanName, T existingBean, BeanDefinition<T> beanDefinition){
        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        Class<?> clazz = existingBean.getClass();

        //todo 获取该Bean类的所有PropertyDescriptor --> 生命周期开始为在XMl扫描时按需调用
        Map<String, PropertyDescriptor> beanPropertyMap = PropertyUtils.getBeanPropertyMap(clazz);

        Map<String, Class<?>> propertyTypeMap = PropertyUtils.getPropertyTypeMap(clazz);

        for(PropertyValue pv : propertyValues.getPropertyValues()){

            String propertyName = pv.getName();
            Object propertyValue = pv.getValue();

            try {
                //todo xml配置中 普通属性不能配置类型 应该以其他方式获取
//                Class<?> propertyType = clazz.getDeclaredField(propertyName).getType();
                PropertyDescriptor pd = beanPropertyMap.get(propertyName);

                // 从属性类型map中获取属性类型
                Class<?> propertyType = propertyTypeMap.get(propertyName);

                Method setterMethod = pd.getWriteMethod();
                // 引用类型
                if(propertyType == BeanReference.class){

                    BeanReference beanReference = (BeanReference) propertyValue;
                    String referBeanName = beanReference.getName();
                    //todo 获取Bean实例 --> 1、有可能该Bean实例尚未创建 应该重写为阻塞等待 （不会有这种情况，因为是懒汉式的设计）
                    //                    2、有可能会发生循环依赖，在后面再解决
                    Object innerBean = getBean(referBeanName);

                    setterMethod.invoke(existingBean,innerBean);

                }else if(propertyType == BeanDefinition.class){
                    // Bean类型（级联定义了一个Bean）
                    //todo 先根据Bean定义创建Bean实例  --> 有可能破坏单例 需要保存在多例注册表中
                    BeanDefinition<?> innerBeanDefinition = (BeanDefinition<?>) propertyValue;
                    Object innerBean = createBean(beanName,innerBeanDefinition);
                    setterMethod.invoke(existingBean,innerBean);
                }else {
                    // 普通属性 直接使用Setter方法注入
                    setterMethod.invoke(existingBean, propertyValue);
                    //todo 需要一个类型转换适配器，否则任何propertyValue都是String类型的
                    setterMethod.invoke(existingBean,
                            PropertyUtils.propertyValueTypeConversion((String)propertyValue,propertyType));
                }

            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化bean的逻辑方法
     */
    public <T> T initializeBean(String beanName, T bean, BeanDefinition<T> beanDefinition) throws InvocationTargetException, IllegalAccessException {

        // 初始化之前执行后置处理器
        T wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean,beanName);
        // 执行自定义初始化方法
        invokeInitMethods(wrappedBean,beanName,beanDefinition);
        // 初始化之后执行后置处理器
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }

    /**
     * 在bean初始化之前 执行它绑定的bean后置处理器
     * @param existingBean 已实例化的bean对象
     * @param beanName bean名字
     * @return 处理过后的bean对象
     */
    public <T> T applyBeanPostProcessorsBeforeInitialization(T existingBean, String beanName){

        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();

        T result = existingBean;

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            T current = beanPostProcessor.postProcessBeforeInitialization(result,beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }


    /**
     * 在bean初始化之后 执行它绑定的bean后置处理器
     * @param existingBean 已实例化的bean对象
     * @param beanName bean名字
     * @return 处理过后的bean对象
     */
    public <T> T applyBeanPostProcessorsAfterInitialization(T existingBean, String beanName){

        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();

        T result = existingBean;

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            T current = beanPostProcessor.postProcessAfterInitialization(result,beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 对指定bean执行自定义的初始化方法
     * @param existingBean 已实例化的bean
     * @param beanName bean名字
     */
    public <T> void invokeInitMethods(T existingBean, String beanName, BeanDefinition<T> beanDefinition) throws InvocationTargetException, IllegalAccessException {

        boolean isInitializingBean = (existingBean instanceof InitializingBean &&
                !"afterPropertiesSet".equals(beanDefinition.getInitMethodName()));

        if(isInitializingBean){
            ((InitializingBean) existingBean).afterPropertiesSet();
        }

        String initMethodName = beanDefinition.getInitMethodName();
        if(StrUtil.isNotBlank(initMethodName)){
            Method initMethod = ClassUtil.getPublicMethod(existingBean.getClass(), initMethodName);
            if(initMethod == null){
                throw new BeansException("the bean named [" + beanName + "] specify initialization method ["+ initMethodName +"] does not exist");
            }
            initMethod.invoke(existingBean);
        }
    }

    /**
     * 判断bean是否实现了DisposableBean接口 或者指定了destroy()方法，若是则注册进注册表中
     */
    protected  <T> void registerDisposableBeanIfNecessary(String beanName, T bean, BeanDefinition<T> beanDefinition){

        // 只有单例bean才能执行自定义的destroy
        if(beanDefinition.isSingleton()){
            // 当bean实现了DisposableBean接口 或者指定了destroy()方法时
            if(bean instanceof DisposableBean || StrUtil.isNotBlank(beanDefinition.getDestroyMethodName())){
                registerDisposableBean(beanName,new DisposableBeanAdapter(beanName,bean,beanDefinition));
            }
        }
    }

}
