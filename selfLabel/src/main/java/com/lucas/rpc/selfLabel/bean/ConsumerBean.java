package com.lucas.rpc.selfLabel.bean;

import com.lucas.rpc.client.SpiderProxy;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 服务调取方bean
 * @create: 2021-08-31 23:55
 **/
@Data
public class ConsumerBean implements FactoryBean {
    /**
     * 接口名称
     */
    private String nozzle;
    /**
     * 服务别名分组信息
     */
    private String alias;

    @Override
    public Object getObject() throws Exception {
        Object proxy = SpiderProxy.getProxy(alias, getObjectType());
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        try{
            return Class.forName(nozzle);
        }catch(Exception e){
            return null;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    // 利用动态代理的方式中间使用
}
