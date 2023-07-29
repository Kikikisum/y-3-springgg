package com.example.y3spring.context.beans.utils;

import jakarta.annotation.Nullable;

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

    /**
     * 用于获取Bean类的每种属性类型
     */
    private static final Map<Class<?>, Map<String,Class<?>>> BEANS_PROPERTY_TYPE_MAP;

    static {
        BEANS_PROPERTY_MAP = new HashMap<>();
        BEANS_PROPERTY_TYPE_MAP = new HashMap<>();
    }

    public static void addBeanPropertyMap(Class<?> clazz, Map<String,PropertyDescriptor> map){
        BEANS_PROPERTY_MAP.putIfAbsent(clazz,map);
    }

    /**
     * 根据指定类的类型clazz获取其propertyDescriptorMap
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
                Class<?> propertyType=field.getType();
                PropertyDescriptor pv = new PropertyDescriptor(propertyName, clazz);
                descriptorMap.put(propertyName,pv);
                addPropertyType(clazz,propertyName,propertyType);
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
    public static void addPropertyDescriptor(Class<?> clazz, String propertyName, @Nullable Class<?> anotherType){
        Method setterMethod;
        Method getterMethod;
        PropertyDescriptor pd;

        try {
            Field field = clazz.getDeclaredField(propertyName);
            Class<?> propertyType = field.getType();


            pd = new PropertyDescriptor(propertyName, clazz);

            // 添加进map中
            Map<String, PropertyDescriptor> descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            if(descriptorMap == null){
                BEANS_PROPERTY_MAP.putIfAbsent(clazz,new HashMap<>());
                descriptorMap = BEANS_PROPERTY_MAP.get(clazz);
            }
            descriptorMap.put(propertyName,pd);

            // 检查是否有设置另外的类型
            if(anotherType != null){
                addPropertyType(clazz, propertyName, anotherType);
            }else {
                addPropertyType(clazz,propertyName,propertyType);
            }

        } catch (NoSuchFieldException | IntrospectionException e) {
            e.printStackTrace();
        }
    }
    public static void addPropertyType(Class<?> clazz, String propertyName, Class<?> propertyType){
        Map<String, Class<?>> propertyTypeMap = BEANS_PROPERTY_TYPE_MAP.get(clazz);
        if(propertyTypeMap == null){
            synchronized (PropertyUtils.class){
                BEANS_PROPERTY_TYPE_MAP.putIfAbsent(clazz,new HashMap<>());
                propertyTypeMap = BEANS_PROPERTY_TYPE_MAP.get(clazz);
            }
        }
        propertyTypeMap.put(propertyName,propertyType);
    }

    public static Class<?> getPropertyType(Class<?> clazz, String propertyName){
        Map<String, Class<?>> propertyTypeMap = BEANS_PROPERTY_TYPE_MAP.get(clazz);
        if(propertyTypeMap == null){
            throw new IllegalArgumentException("该Bean类型不存在");
        }
        Class<?> propertyType = propertyTypeMap.get(propertyName);
        if(propertyType == null){
            throw new IllegalArgumentException("该Bean不存在该属性名: " + propertyName);
        }
        return propertyType;
    }

    public static Map<String,Class<?>> getPropertyTypeMap(Class<?> clazz){
        Map<String, Class<?>> propertyTypeMap = BEANS_PROPERTY_TYPE_MAP.get(clazz);
        if(propertyTypeMap == null){
            throw new IllegalArgumentException("该Bean类型不存在");
        }
        return propertyTypeMap;
    }
}
