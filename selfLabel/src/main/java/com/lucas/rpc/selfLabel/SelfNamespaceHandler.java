package com.lucas.rpc.selfLabel;

import com.lucas.rpc.selfLabel.bean.ConsumerBean;
import com.lucas.rpc.selfLabel.bean.ProviderBean;
import com.lucas.rpc.selfLabel.bean.RegisterCenterBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: spring标签命名空间处理器  用于解析自定义标签
 * @create: 2021-08-30 23:29
 **/
public class SelfNamespaceHandler extends NamespaceHandlerSupport {

    /**
     * 指定标签的解析方式
     */
    @Override
    public void init() {
        registerBeanDefinitionParser("server",new SelfBeanDefinitionParser(RegisterCenterBean.class));
        registerBeanDefinitionParser("provider",new SelfBeanDefinitionParser(ProviderBean.class));
        registerBeanDefinitionParser("consumer",new SelfBeanDefinitionParser(ConsumerBean.class));
    }
}
