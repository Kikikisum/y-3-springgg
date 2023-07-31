package com.example.y3spring.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.example.y3spring.beans.factory.config.BeanDefinition;
import com.example.y3spring.beans.factory.config.BeanReference;
import com.example.y3spring.beans.factory.config.PropertyValue;
import com.example.y3spring.beans.factory.config.PropertyValues;
import com.example.y3spring.beans.factory.utils.PropertyUtils;
import com.example.y3spring.beans.factory.io.Resource;
import com.example.y3spring.beans.factory.io.ResourceLoader;
import com.example.y3spring.beans.factory.exception.BeansException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * 从xml文件读取bean定义的读取器
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader{

    public static final String BEAN_ELEMENT_TAG = "bean";
    public static final String PROPERTY_ELEMENT_TAG = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";

    public XmlBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry beanDefinitionRegistry) {
        super(resourceLoader, beanDefinitionRegistry);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (IOException e) {
            throw new BeansException("");
        }
    }

    /**
     * 读取XML配置文件中的BeanDefinitions
     * @param inputStream xml文件的二进制流
     */
    public void doLoadBeanDefinitions(InputStream inputStream){
        Document document = XmlUtil.readXML(inputStream);
        // 根标签
        Element root = document.getDocumentElement();
        // 根标签下的子节点
        NodeList childNodes = root.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            // 如果子节点是一个标签 则开始解析
            if(childNodes.item(i) instanceof Element){
                Element element = (Element) childNodes.item(i);
                // 如果该标签为bean标签
                if(BEAN_ELEMENT_TAG.equals(element.getTagName())){
                    //解析bean标签
                    String id = element.getAttribute(ID_ATTRIBUTE);
                    String name = element.getAttribute(NAME_ATTRIBUTE);
                    String className = element.getAttribute(CLASS_ATTRIBUTE);

                    Class clazz;
                    try {
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new BeansException("不存在该类型: [ "+ className + "] 的类",e);
                    }
                    doLoadBeanDefinition(element);
                }
            }
        }
    }

    /**
     * 加载指定Bean标签的beanDefinition实际逻辑
     * @param beanElement bean标签
     */
    public BeanDefinition<?> doLoadBeanDefinition(Element beanElement){
        //解析bean标签
        String id = beanElement.getAttribute(ID_ATTRIBUTE);
        String name = beanElement.getAttribute(NAME_ATTRIBUTE);
        String className = beanElement.getAttribute(CLASS_ATTRIBUTE);
        String initMethodName = beanElement.getAttribute(INIT_METHOD_ATTRIBUTE);
        String destroyMethodName = beanElement.getAttribute(DESTROY_METHOD_ATTRIBUTE);
        String scope = beanElement.getAttribute(SCOPE_ATTRIBUTE);

        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("不存在该类型: [ "+ className + "] 的类",e);
        }
        // 存放Bean定义
        BeanDefinition<?> beanDefinition = new BeanDefinition<>();
        beanDefinition.setType(clazz);
        PropertyValues propertyValues = new PropertyValues();

        // 如果id不为空 则beanName为id
        String beanName = StrUtil.isNotBlank(id) ? id : name;

        // 若都为空 则beanName为类名（首字母小写）
        if(StrUtil.isBlank(beanName)){
            beanName = className.substring(className.lastIndexOf('.'));
            beanName = beanName.substring(0,1).toLowerCase(Locale.ROOT) + beanName.substring(1);
        }

        // 查询注册表中是否有同名的bean
        if(getBeanDefinitionRegistry().containsBeanDefinition(beanName)){
            throw new BeansException("不能注册重名的Bean，注册失败");
        }

        // 读取bean标签下的子标签（property，bean等）
        NodeList subNodes = beanElement.getChildNodes();
        int subLength = subNodes.getLength();
        for (int j = 0; j < subLength; j++) {
            if(subNodes.item(j) instanceof Element){
                Element subElement = (Element) subNodes.item(j);
                String subTagName = subElement.getTagName();
                // 如果是property标签
                if(PROPERTY_ELEMENT_TAG.equals(subTagName)){
                    String propertyName = subElement.getAttribute(NAME_ATTRIBUTE);
                    String propertyValue = subElement.getAttribute(VALUE_ATTRIBUTE);

                    PropertyValue pv;
                    // 是一个值
                    if(StrUtil.isNotBlank(propertyValue)){
                        //todo 这里的value是字符串类型 怎么转换为原类型？String可以转换成任意基本类型，而value只处理基本类型的属性
                        pv = new PropertyValue(propertyName, propertyValue);

                        PropertyUtils.addPropertyDescriptor(clazz,propertyName,null);

                    }else {
                        // 是一个引用 则propertyRef是引用的beanName
                        String propertyRef = subElement.getAttribute(REF_ATTRIBUTE);
                        BeanReference beanReference = new BeanReference(propertyRef);

                        // 引用作为属性值，自动装配时会注入bean实例
                        pv = new PropertyValue(propertyName,beanReference);

                        PropertyUtils.addPropertyDescriptor(clazz,propertyName,BeanReference.class);
                    }
                    beanDefinition.setPropertyValues(propertyValues);
                    // 将bean定义注册进注册表中
                    getBeanDefinitionRegistry().registerBeanDefinition(beanName,beanDefinition);

                    // 添加属性到列表中
                    propertyValues.addPropertyValue(pv);

                }else if(BEAN_ELEMENT_TAG.equals(subTagName)){
                    //todo 如果是bean标签 则创建一个内置BeanDefinition 先不处理 嵌套太多 代码复杂

                    // 递归处理嵌套bean
                    BeanDefinition<?> subBeanDefinition = doLoadBeanDefinition(subElement);

                    String subBeanName = subElement.getAttribute(ID_ATTRIBUTE);

                    PropertyValue pv = new PropertyValue(subBeanName,subBeanDefinition);
                    PropertyUtils.addPropertyDescriptor(clazz,subBeanName,BeanDefinition.class);

                    propertyValues.addPropertyValue(pv);

                }
            }
        }
        beanDefinition.setPropertyValues(propertyValues);
        beanDefinition.setInitMethodName(initMethodName);
        beanDefinition.setDestroyMethodName(destroyMethodName);

        // 设置作用域
        beanDefinition.setSingleton(scope.equals(BeanDefinition.SCOPE_SINGLETON));
        beanDefinition.setPrototype(scope.equals(BeanDefinition.SCOPE_PROTOTYPE));

        // 将bean定义注册进注册表中
        getBeanDefinitionRegistry().registerBeanDefinition(beanName,beanDefinition);

        return beanDefinition;
    }

    @Override
    public void loadBeanDefinitions(Resource[] resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }
}