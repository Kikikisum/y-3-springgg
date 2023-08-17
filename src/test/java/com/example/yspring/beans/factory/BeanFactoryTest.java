package com.example.yspring.beans.factory;

import com.example.y3spring.beans.factory.BeanFactory;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.beans.factory.config.BeanReference;
import com.example.y3spring.beans.factory.config.PropertyValue;
import com.example.y3spring.beans.factory.config.PropertyValues;
import com.example.y3spring.beans.factory.support.BeanDefinitionRegistry;
import com.example.y3spring.beans.factory.support.DefaultListableBeanFactory;
import com.example.y3spring.beans.factory.utils.PropertyUtils;
import com.example.yspring.entity.Car;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.util.Map;

public class BeanFactoryTest {
    /**
     * 测试创建Bean以及注入属性
     */
    @Test
    public void testCreateBeanAndPropertyInjection(){
        // 模拟从xml中读取bean的属性 并封装在PropertyValue中
        PropertyValue brand = new PropertyValue("brand", "宝马");
        PropertyValue price = new PropertyValue("price", 12121212);
        PropertyValue owner = new PropertyValue("owner", "kikikisum");

        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(brand);
        propertyValues.addPropertyValue(price);
        propertyValues.addPropertyValue(owner);
        // 读取对Bean的定义
        BeanDefinition<Car> carBeanDefinition = new BeanDefinition<>(Car.class, propertyValues);

        // 将Bean定义注册进注册表
        BeanDefinitionRegistry definitionRegistry = new DefaultListableBeanFactory();
        definitionRegistry.registerBeanDefinition("superCar",carBeanDefinition);

        // 使用xml获取bean的全类名，并将类的所有属性的propertyDescriptor注册到缓存中
        PropertyUtils.addPropertyDescriptor(Car.class);
        Map<String, PropertyDescriptor> beanPropertyMap = PropertyUtils.getBeanPropertyMap(Car.class);

        // 使用BeanFactory创建并获取bean实例
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        Object car = beanFactory.getBean("superCar");

        System.out.println(car);
        System.out.println(car.getClass());
    }

    /**
     * 测试Bean注入功能
     * 1、直接注入（级联Bean）
     */
    @Test
    public void testBeanPropertyBeanStrightInjection(){
        // 内嵌Bean定义
        PropertyValue innerBrand = new PropertyValue("brand", "垃圾厂的轮胎");
        PropertyValues innerPropertyValues = new PropertyValues();
        innerPropertyValues.addPropertyValue(innerBrand);

        // 外层Bean定义
        PropertyValue brand = new PropertyValue("brand", "宝马");
        PropertyValue price = new PropertyValue("price", 2000000);
        PropertyValue owner = new PropertyValue("owner", "sillybaka");
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(brand);
        propertyValues.addPropertyValue(price);
        propertyValues.addPropertyValue(owner);

        BeanDefinition<Car> beanDefinition = new BeanDefinition<>(Car.class, propertyValues);

        // 将bean定义注册进注册表
        BeanDefinitionRegistry beanDefinitionRegistry = new DefaultListableBeanFactory();
        beanDefinitionRegistry.registerBeanDefinition("niubiCar",beanDefinition);

        // 若有内联bean，则需要手动添加PropertyDescriptor

        PropertyUtils.addPropertyDescriptor(Car.class,"brand",null);
        PropertyUtils.addPropertyDescriptor(Car.class,"price",null);
        PropertyUtils.addPropertyDescriptor(Car.class,"owner",null);
        PropertyUtils.addPropertyDescriptor(Car.class,"carRoll",BeanDefinition.class);

        // 创建Bean
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        Object niubiCar = beanFactory.getBean("niubiCar");

        System.out.println(niubiCar);
    }

    @Test
    public void testBeanPropertyBeanReferenceInjection(){
        // 引用Bean定义
        PropertyValue refBrand = new PropertyValue("brand", "我是被引用的轮胎");
        PropertyValues refPropertyValues = new PropertyValues();
        refPropertyValues.addPropertyValue(refBrand);

        // 将bean定义注册进注册表
        BeanDefinitionRegistry beanDefinitionRegistry = new DefaultListableBeanFactory();

        // Bean定义
        BeanReference beanReference = new BeanReference();
        beanReference.setName("carRoll");
        PropertyValue carRoll = new PropertyValue("carRoll", beanReference);

        PropertyValue brand = new PropertyValue("brand", "宝马");
        PropertyValue price = new PropertyValue("price", 12121212);
        PropertyValue owner = new PropertyValue("owner", "kikikisum");
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(brand);
        propertyValues.addPropertyValue(price);
        propertyValues.addPropertyValue(owner);
        propertyValues.addPropertyValue(carRoll);

        BeanDefinition<Car> beanDefinition = new BeanDefinition<>(Car.class, propertyValues);

        // 将bean定义注册进注册表
        beanDefinitionRegistry.registerBeanDefinition("niubiCar",beanDefinition);


        PropertyUtils.addPropertyDescriptor(Car.class,"brand",null);
        PropertyUtils.addPropertyDescriptor(Car.class,"price",null);
        PropertyUtils.addPropertyDescriptor(Car.class,"owner",null);

        // 创建Bean
        BeanFactory beanFactory = new DefaultListableBeanFactory();
        Object niubiCar = beanFactory.getBean("niubiCar");

        System.out.println(niubiCar);
    }
}
