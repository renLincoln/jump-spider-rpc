package com.lucas.rpc.client;

import com.lucas.msg.dto.SpiderRequest;
import com.lucas.msg.future.SyncWrite;
import com.lucas.rpc.client.service.ProductFind;
import com.luncas.common.dto.ProductInfoDto;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: spider动态代理工具
 * @create: 2021-09-12 22:40
 **/
@Slf4j
public class SpiderProxy<T> {
    /**
     * 超时时间
     */
    private static final int timeOut = 30;

    public static<T> T getProxy(String serviceName,Class<T> interfaceClass){
        T proxyObject = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{interfaceClass},
                (p, m, a) -> {
                    // 获取接口的方法接口，并判断请求的方法是否在接口中
                    Method[] methods = interfaceClass.getMethods();
                    for(Method method: methods){
                        if(method.getName().equals(m.getName())){
                            // 封装请求对象
                            SpiderRequest request = SpiderRequest.builder().methodName(m.getName())
                                    .args(a).paramTypes(m.getParameterTypes())
                                    .alias(serviceName)
                                    .nozzle(interfaceClass.getName())
                                    .build();
                            // 在接口中，通过msg服务进行调用
                            ChannelFuture future = ProductFind.tryGetServerChannelFuture(serviceName, interfaceClass);
                            Channel channel = future.channel();
                            request.setChannel(channel);
                            // 反序列化，并返回结果
                            return SyncWrite.writeAndSync(channel,request,timeOut);
                        }
                    }
                    // 否则提示消息，返回null值
                    log.warn("当前调用方法非接口代理方法：{}，rpc调用失败",m.getName());
                    return null;
                });
        return proxyObject;
    }
}
