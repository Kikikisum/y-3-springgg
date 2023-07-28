package com.example.y3spring.context.beans.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 底层使用PropertyDescriptor
 */
public class PropertyUtils {
    /**
     * 使用Bean类获取其所有属性的PropertyDescriptor Map
     */
    private static final Map<Class<?>, Map<String, PropertyDescriptor>> BEANS_PROPERTY_MAP;

    static {
        BEANS_PROPERTY_MAP = new HashMap<>();
    }

    public static void addBeanPropertyMap(Class<?> clazz, Map<String,PropertyDescriptor> map){
        BEANS_PROPERTY_MAP.putIfAbsent(clazz,map);
    }

    /**
     * 根据指定类的clazz获取其propertyDescriptorMap
     * @param clazz
     * @return
     */
    public static Map<String,PropertyDescriptor> getBeanPropertyMap(Class<?> clazz){
        Map<String, PropertyDescriptor> descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
        if(descriptorMap == null){
            // 懒汉式单例加载
            synchronized (PropertyUtils.class){
                descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
                if(descriptorMap == null){
                    addPropertyDescriptor(clazz);
                }
                descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            }
        }
        return descriptorMap;
    }


    /**
     * 根据指定类的class将其所有属性的PropertyDescriptor添加到map中
     * @param clazz 指定类的class
     */
    public static void addPropertyDescriptor(Class<?> clazz){
        Map<String, PropertyDescriptor> descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
        if(descriptorMap == null){
            BEANS_PROPERTY_MAP.putIfAbsent(clazz,new HashMap<>());
            descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
        }
        // 获取该类的所有属性的Descriptor
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            String propertyName = field.getName();
            try {
                PropertyDescriptor pv = new PropertyDescriptor(propertyName, clazz);
                descriptorMap.put(propertyName,pv);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据指定类的的class和propertyName添加PropertyDescriptor到map中
     * @param clazz 指定类的class
     * @param propertyName 属性名
     */
    public static void addPropertyDescriptor(Class<?> clazz, String propertyName){
        Method setterMethod;
        Method getterMethod;
        PropertyDescriptor pd;

        try {
            Field field = clazz.getDeclaredField(propertyName);
            Class<?> propertyType = field.getType();

            // 获取setter和getter方法
            String methodSuffix = propertyName.substring(0,1).toUpperCase() + propertyName.substring(1);

            setterMethod = propertyType.getDeclaredMethod("set" + methodSuffix, propertyType);
            getterMethod = propertyType.getDeclaredMethod("get" + methodSuffix, propertyType);


            pd = new PropertyDescriptor(propertyName, getterMethod, setterMethod);

            // 添加进map中
            Map<String, PropertyDescriptor> descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            if(descriptorMap == null){
                BEANS_PROPERTY_MAP.putIfAbsent(clazz,new HashMap<>());
                descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            }
            descriptorMap.put(propertyName,pd);

        } catch (NoSuchFieldException | NoSuchMethodException | IntrospectionException e) {
            e.printStackTrace();
        }
    }
}
