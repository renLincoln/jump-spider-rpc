package com.lucas.rpc.selfLabel.bean;

import lombok.Data;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: 服务注册中心Bean
 * @create: 2021-08-31 23:57
 **/
@Data
public class RegisterCenterBean {
    /**
     * 注册中心地址
     */
    private String host;
    /**
     * 注册中心端口
     */
    private String port;

//    @PostConstruct
//    public void registerCenterInit(){
//        // 初始化redis相关 host与port
//        JedisUtils.PORT = Integer.parseInt(port);
//        JedisUtils.ADDR_ARRAY = host;
//    }
//    public String getHost() {
//        return host;
//    }
//
//    public void setHost(String host) {
//        this.host = host;
//    }
//
//    public String getPort() {
//        return port;
//    }
//
//    public void setPort(String port) {
//        this.port = port;
//    }
}
