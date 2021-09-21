package com.luncas.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 提供服务的基本信息
 * @create: 2021-09-12 21:02
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDto {
    /**
     * 服务提供者通讯ip
     */
    private String host;
    /**
     * 服务提供者通讯端口port
     */
    private int port;
    /**
     * 服务提供者的service接口class全路径
     */
    private String serviceClassName;
}
