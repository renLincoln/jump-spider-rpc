package com.lucas.rpc.client.service;

import com.alibaba.fastjson.JSON;
import com.lucas.common.constant.RedisKey;
import com.lucas.jedis.util.RedisUtil;
import com.lucas.msg.client.ClientSocket;
import com.luncas.common.dto.ProductInfoDto;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 服务发现
 * @create: 2021-09-12 22:59
 **/
@Slf4j
public class ProductFind {
    /**
     * 客户端维护的服务发现缓存
     */
//    public static Map<String,ConcurrentHashMap<String,ChannelFuture>> serverChannelFutureMap = new ConcurrentHashMap();

    public static Map<String,ChannelFuture> serverChannelFutureMap = new ConcurrentHashMap();
    /**
     * @Author: ren
     * @Description: 客户端服务发现
     * @Param: [seriesName, interfaceClass]
     * @return: com.luncas.common.dto.ProductInfoDto
     * @Date: 2021/9/12
     */
    public static ProductInfoDto productServiceFind(String seriesName,Class interfaceClass){
        String serviceInfo = RedisUtil.srandmember(RedisKey.SERVICE_NAME + seriesName);
        if (serviceInfo != null) {
            // 成功获取
            ProductInfoDto productInfoDto = JSON.parseObject(serviceInfo, ProductInfoDto.class);
            if(interfaceClass.getName().equals(productInfoDto.getServiceClassName())){
                return productInfoDto;
            }
            log.error("从注册中心获取服务：{}与当前接口：{}不一致", productInfoDto.getServiceClassName(),interfaceClass.getName());
        } else {
            // 获取失败
            log.error("从注册中心获取服务：{}失败，请检查服务是否正常注册", seriesName);
        }
        return null;
    }

    /**
     * @Author: ren
     * @Description: 尝试获取服务，并返回服务连接的ChannelFuture
     * @Param: [seriesName, interfaceClass]
     * @return: io.netty.channel.ChannelFuture
     * @Date: 2021/9/12
     */
    public static ChannelFuture tryGetServerChannelFuture(String seriesName,Class interfaceClass) throws InterruptedException {
        if (serverChannelFutureMap.get(seriesName) == null) {
            // 在本地内存中未能获取到服务对应的hannelFuture
            // 尝试从redis服务注册中心发现服务
            ProductInfoDto productInfoDto = productServiceFind(seriesName, interfaceClass);
            if (productInfoDto == null) {
                return null;
            }
            // 声明客户端
            ClientSocket clientSocket = new ClientSocket(productInfoDto.getHost(), productInfoDto.getPort());
            // 建立客户端连接
            new Thread(clientSocket).start();
            for (int i = 0; i < 10; i++) {
                if(clientSocket.getFuture()!=null){
                    break;
                }
                Thread.sleep(500);
            }
            // 并将客户端的channel进行存放到本地内存中
            serverChannelFutureMap.put(seriesName,clientSocket.getFuture());
            return clientSocket.getFuture();
        }else{
            return serverChannelFutureMap.get(seriesName);
        }
    }
}
