package com.example.y3spring.beans.factory.config;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    /**
     * 在实例化Bean前调用的方法，用于生成代理对象代替原Bean实例
     * @param beanClass bean的实际类型
     * @param beanName bean名字
     * @return 成功创建代理对象则为代理对象
     *         否则返回null
     */
    default Object postProcessBeforeInstantiation(Class<?> beanClass,String beanName){
        return null;
    }

    /**
     * 用于在bean实例化之后，注入属性之前更改要注入的属性列表
     * @param pv 属性列表
     * @param bean bean实例
     * @param beanName bean名字
     * @return 更改后的属性列表
     */
    default PropertyValues postProcessPropertyValues(PropertyValues pv,Object bean,String beanName){
        return null;
    }
}
