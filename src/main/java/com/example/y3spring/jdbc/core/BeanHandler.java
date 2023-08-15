package com.example.y3spring.jdbc.core;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class BeanHandler<T> implements ResultSetHandler<T>{

    private Class<T> beanClass;

    public BeanHandler(Class<T> beanClass){
        this.beanClass = beanClass;
    }

    @Override
    public T handler(ResultSet rs) {
        // 声明自定义对象类型
        T bean = null;
        try {
            bean = beanClass.newInstance();
            // 判断结果集中是否有数据
            if (rs.next()){
                // 通过结果集获取结果集源信息的对象
                ResultSetMetaData metaData = rs.getMetaData();
                // 通过结果集源信息对象获取列数
                int count = metaData.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    // 通过结果集源信息对象获取列名
                    String columnName = metaData.getColumnName(i);
                    // 通过列名获取该列的数据
                    Object value = rs.getObject(columnName);
                    // 创建属性描述器对象，将获取到的值通过该对象的set方法进行赋值
                    PropertyDescriptor pd = new PropertyDescriptor(columnName.toLowerCase(),beanClass);
                    Method writeMethod = pd.getWriteMethod();
                    // 执行set方法，给成员变量赋值
                    writeMethod.invoke(bean,value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }
}
