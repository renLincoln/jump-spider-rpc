package com.lucas.rpc.selfLabel;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 自定义spring标签解析器
 * @create: 2021-08-30 23:28
 **/
public class SelfBeanDefinitionParser<T> extends AbstractSingleBeanDefinitionParser {

    private final Class<T> beanClass;

    public SelfBeanDefinitionParser(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return this.beanClass;
    }

    @Override
    public void doParse(Element element, BeanDefinitionBuilder builder) {
        // 获取自定义element的相关属性
        NamedNodeMap attributes = element.getAttributes();
        // 判空，并提取数据
        if (element != null && attributes.getLength() > 0) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node node = attributes.item(i);
                // 判断node是否属于属性
                if(node.getNodeType() == Node.ATTRIBUTE_NODE && !"id".equals(node.getNodeName())){
                    // 分别获取属性名称与属性值
                    String attributeName = node.getNodeName();
                    String attributeValue = node.getNodeValue();
                    builder.addPropertyValue(attributeName,attributeValue);
                }
            }
        }
    }
}
