package com.example.y3spring.context.beans.factory.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * bean的引用属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanReference {
    /**
     * bean名字
     */
    private String name;
    /**
     * bean类型
     */
    private String type;
}
