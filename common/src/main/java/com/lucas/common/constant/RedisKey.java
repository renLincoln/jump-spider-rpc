package com.lucas.common.constant;

/**
 * redis相关的key值
 */
public interface RedisKey {
    /**
     *  redis key值注释
     * <br>键:
     * <br>值:
     * <br>过期：
     */

    /**
     * redis key值根节点
     */
    String ROOT = "spider:";
    /** ####################### 服务注册中心相关 ####################### */
    /**
     * spider rpc redis前缀
     */
    String SPIDER_RPC = ROOT + "rpc:";
    /**
     *  spider rpc 注册服务前缀
     * <br>键: spider:rpc:service_name:；描述：注册服务前缀
     * <br>值: String
     * <br>过期：
     */
    String SERVICE_NAME = SPIDER_RPC + "service_name:";

    /** ####################### 其它 ####################### */


}
