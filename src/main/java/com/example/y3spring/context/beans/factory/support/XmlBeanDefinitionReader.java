package com.example.y3spring.context.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.example.y3spring.co.io.Resource;
import com.example.y3spring.co.io.ResourceLoader;
import com.example.y3spring.exception.BeansException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader{

    public static final String BEAN_ELEMENT_TAG = "bean";
    public static final String PROPERTY_ELEMENT_TAG = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";

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

    // 封装实际的读取Bean定义逻辑 方便拓展
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

                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new BeansException("不存在该类型: [ "+ className + "] 的类",e);
                    }

                    // 如果id不为空 则beanName为id
                    String beanName = StrUtil.isNotBlank(id) ? id : name;

                    // 若都为空 则beanName为类名（首字母小写）
                    if(StrUtil.isBlank(beanName)){
                        beanName = className.substring(className.lastIndexOf('.'));
                        beanName = beanName.substring(0,1).toLowerCase(Locale.ROOT) + beanName.substring(1);
                    }

                    // 读取bean标签下的子标签（property，bean等）
                    NodeList subNodes = element.getChildNodes();
                    int subLength = subNodes.getLength();
                    for (int j = 0; j < subLength; j++) {
                        if(subNodes.item(j) instanceof Element){
                            Element subElement = (Element) subNodes.item(j);
                            String subTagName = subElement.getTagName();
                            // 如果是property标签
                            if(PROPERTY_ELEMENT_TAG.equals(subTagName)){




                            }else if(BEAN_ELEMENT_TAG.equals(subTagName)){
                                // 如果是bean标签
                            }
                        }
                    }
                }
            }

        }
    }
}